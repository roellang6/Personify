package dev.ran.personify.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import dev.ran.personify.R
import dev.ran.personify.model.Result
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class PersonAdapter(private var pList: List<Result>, private var context : Context, private var adapterClick : AdapterClick) :
    RecyclerView.Adapter<PersonAdapter.DataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.person_data_layout, parent, false
        ),adapterClick
    )

    override fun getItemCount(): Int {
        return pList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.bindData(pList[position], context)
    }

    class DataHolder(v: View, private var itemClick: AdapterClick) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private val vImage : CircleImageView = v.findViewById(R.id.imgPerson)
        private val vDetails : TextView = v.findViewById(R.id.txtDetails)
        private val vName : TextView = v.findViewById(R.id.txtName)
        private val vAge : TextView = v.findViewById(R.id.txtAgeData)
        private val vBirthday : TextView = v.findViewById(R.id.txtBirthDayData)
        private val vEmail : TextView = v.findViewById(R.id.txtEmailData)
        private val vMobile : TextView = v.findViewById(R.id.txtMobileData)
        private val vAddress : TextView = v.findViewById(R.id.txtAddressData)
        private val vContactPerson : TextView = v.findViewById(R.id.txtContactData)
        private val vCPersonMobile : TextView = v.findViewById(R.id.txtContactPersonData)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindData(result: Result, context : Context) {
            Glide.with(context)
                .load(result.picture.large)
                .placeholder(R.drawable.display_img)
                .error(R.drawable.error_img)
                .into(vImage)

            val name  = "${result.name.title} ${result.name.first} ${result.name.last}"
            vName.text = name

            val birthDate = LocalDate.parse(result.dob.date, DateTimeFormatter.ISO_DATE_TIME)
            val age = calculateAge(birthDate, LocalDate.now())
            vAge.text = age.toString()

            val bDau = result.dob.date.split("-")
            val birthdays ="${bDau[1]}/${bDau[2].substring(0, 2)}/${bDau[0]}"
            vBirthday.text = birthdays

            vEmail.text = result.email
            vMobile.text = result.cell

            val address = "${result.location.street.number}, ${result.location.street.name}, ${result.location.city}, ${result.location.state}, ${result.location.country}"
            vAddress.text = address

            vContactPerson.text = "-"
            vCPersonMobile.text = result.phone

            vDetails.setOnClickListener(this)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun calculateAge(birthDate: LocalDate, currentDate: LocalDate): Int {
            return Period.between(birthDate, currentDate).years
        }

        override fun onClick(v: View?) {
            itemClick.getItemPosition(adapterPosition)
        }
    }
}