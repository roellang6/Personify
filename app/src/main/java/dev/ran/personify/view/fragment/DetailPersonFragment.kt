package dev.ran.personify.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import dev.ran.personify.R
import dev.ran.personify.model.Result
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DetailPersonFragment : Fragment() {
    private lateinit var vLayout : View
    private lateinit var imgBacks : ImageView
    private val personList = mutableListOf<Result>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vLayout = inflater.inflate(R.layout.fragment_detail_person, container, false)
        imgBacks = vLayout.findViewById(R.id.imgBacks)

        imgBacks.setOnClickListener{
            val gson = Gson()
            val jsonString = gson.toJson(personList[0])
            val detailFrag = PersonFragment ()
            val args = Bundle()
            args.putString("Back",  "1")
            args.putString("result",  jsonString)
            detailFrag.arguments = args

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frameId, detailFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        personList.clear()
        val modelResource = arguments?.getString("result")
        displayDetails(modelResource)
        return vLayout
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayDetails(modelResource: String?) {
        val tvImage : CircleImageView = vLayout.findViewById(R.id.imgPersonD)
        val tvName : TextView = vLayout.findViewById(R.id.txtNameD)
        val tvGender : TextView = vLayout.findViewById(R.id.txtGenderData)
        val tvAge : TextView = vLayout.findViewById(R.id.txtAgeDataD)
        val tvBirthday : TextView = vLayout.findViewById(R.id.txtBirthDayDataD)
        val tvEmail : TextView = vLayout.findViewById(R.id.txtEmailDataD)
        val tvMobile : TextView = vLayout.findViewById(R.id.txtMobileDataD)
        val tvAddress : TextView = vLayout.findViewById(R.id.txtAddressDataD)
        val tvPostCode : TextView = vLayout.findViewById(R.id.txtPostalData)
        val tvLat : TextView = vLayout.findViewById(R.id.txtCoordinateLatData)
        val tvOffset : TextView = vLayout.findViewById(R.id.txtOffsetData)
        val tvDescription : TextView = vLayout.findViewById(R.id.txtDescriptionData)
        val tvLong : TextView = vLayout.findViewById(R.id.txtCoordinateLongData)
        val tvContactPerson : TextView = vLayout.findViewById(R.id.txtContactDataD)
        val tvCPersonMobile : TextView = vLayout.findViewById(R.id.txtContactPersonDataD)
        val tvRegisterDate : TextView = vLayout.findViewById(R.id.txtRegDateData)
        val tvRegisterAge : TextView = vLayout.findViewById(R.id.txtRegAgeData)
        val tvIdName : TextView = vLayout.findViewById(R.id.txtIdNameData)
        val tvIdValue : TextView = vLayout.findViewById(R.id.txtValueData)
        val tvNat : TextView = vLayout.findViewById(R.id.txtNatData)
        val tvUid : TextView = vLayout.findViewById(R.id.txtUidData)
        val tvUser : TextView = vLayout.findViewById(R.id.txtUsernameData)
        val tvPas : TextView = vLayout.findViewById(R.id.txtPasswordData)
        val tvSalt : TextView = vLayout.findViewById(R.id.txtSaltData)
        val tvMd5 : TextView = vLayout.findViewById(R.id.txtMd5Data)
        val tvSha1 : TextView = vLayout.findViewById(R.id.txtSha1Data)
        val tvSha256 : TextView = vLayout.findViewById(R.id.txtSha256Data)

        if (!modelResource.isNullOrEmpty()) {
            val result = Gson().fromJson(modelResource, Result::class.java)
            personList.addAll(listOf(result))
            Glide.with(requireContext())
                .load(result.picture.large)
                .placeholder(R.drawable.display_img)
                .error(R.drawable.error_img)
                .into(tvImage)

            val name  = "${result.name.title} ${result.name.first} ${result.name.last}"
            tvName.text = name

            tvGender.text = result.gender

            val birthDate = LocalDate.parse(result.dob.date, DateTimeFormatter.ISO_DATE_TIME)
            val age = calculateAge(birthDate, LocalDate.now())
            tvAge.text = age.toString()

            val bDau = result.dob.date.split("-")
            val birthdays ="${bDau[1]}/${bDau[2].substring(0, 2)}/${bDau[0]}"
            tvBirthday.text = birthdays

            tvEmail.text = result.email
            tvMobile.text = result.cell

            val address = "${result.location.street.number}, ${result.location.street.name}, ${result.location.city}, ${result.location.state}, ${result.location.country}"
            tvAddress.text = address

            tvContactPerson.text = "-"
            tvCPersonMobile.text = result.phone

            tvPostCode.text = result.location.postcode.toString()
            tvLat.text = result.location.coordinates.latitude
            tvLong.text = result.location.coordinates.longitude

            tvOffset.text = result.location.timezone.offset
            tvDescription.text = result.location.timezone.description

            val regDate = result.registered.date.split("-")
            val dateR ="${regDate[1]}/${regDate[2].substring(0, 2)}/${regDate[0]}"
            tvRegisterDate.text = dateR

            tvRegisterAge.text = result.registered.age.toString()
            tvRegisterAge.text = result.registered.age.toString()
            tvIdName.text = result.id.name
            tvIdValue.text = result.id.value
            tvNat.text = result.nat
            tvUid.text = result.login.uuid
            tvUser.text = result.login.username
            tvPas.text = result.login.password
            tvSalt.text = result.login.salt
            tvMd5.text = result.login.md5
            tvSha1.text = result.login.sha1
            tvSha256.text = result.login.sha256
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateAge(birthDate: LocalDate, currentDate: LocalDate): Int {
        return Period.between(birthDate, currentDate).years
    }

}