package dev.ran.personify.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import dev.ran.personify.R
import dev.ran.personify.adapter.AdapterClick
import dev.ran.personify.adapter.PersonAdapter
import dev.ran.personify.model.Result
import dev.ran.personify.viewmodel.PersonViewModel

class PersonFragment : Fragment(), AdapterClick {
    private lateinit var view : View
    private lateinit var personViewModel : PersonViewModel
    private lateinit var personAdapter : PersonAdapter
    private lateinit var personRv : RecyclerView
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private val personList = mutableListOf<Result>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_person, container, false)
        personRv = view.findViewById(R.id.personDataRv)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            startInitialise()
        }

        val backHere = arguments?.getString("Back")
        if(backHere == "1"){
            personList.clear()
            val resultData = arguments?.getString("result")
            val result = Gson().fromJson(resultData, Result::class.java)
            personList.addAll(listOf(result))
            setUpUi(personList)
        }else{
            startInitialise()
        }
        return view
    }

    private fun startInitialise() {
        setUpViewModel()
        setUpObserver()
    }

    private fun setUpViewModel() {
        personViewModel = ViewModelProvider(this)[PersonViewModel::class.java]
    }

    private fun setUpObserver() {
        personViewModel.refreshPerson()
        personViewModel.livePerson.observe(viewLifecycleOwner) { person ->
            personList.clear()
           if(person.results.isNotEmpty()){
               personList.addAll(person.results)
           }else{
               toastMessage("Something went wrong")
           }
            personViewModel.livePerson.removeObservers(this)
            setUpUi(person.results)
        }
        personViewModel.errorPerson.observe(viewLifecycleOwner){error->
            if(error){
                toastMessage("Unable to connect")
            }
            personViewModel.errorPerson.removeObservers(this)
        }
    }

    private fun setUpUi(result: List<Result>){
        personAdapter = PersonAdapter(result, requireContext(), this)
        personRv.layoutManager = LinearLayoutManager(requireContext())
        personRv.adapter = personAdapter
        swipeRefresh.isRefreshing = false
    }

    private fun toastMessage(m : String){
        Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show()
    }

    override fun getItemPosition(position: Int) {
        val gson = Gson()
        val jsonString = gson.toJson(personList[position])
        val detailFrag = DetailPersonFragment ()
        val args = Bundle()
        args.putString("result", jsonString)
        detailFrag.arguments = args

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameId, detailFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}