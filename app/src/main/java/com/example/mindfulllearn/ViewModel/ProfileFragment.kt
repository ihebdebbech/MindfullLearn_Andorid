package com.example.mindfulllearn.ViewModel

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.R
import com.example.mindfulllearn.databinding.FragmentBlankBinding
import com.example.mindfulllearn.databinding.FragmentProfileBinding
import com.example.mindfulllearn.ViewModel.ModifyProfileViewModel
import com.google.android.material.internal.ViewUtils
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private val updateViewModel: ModifyProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var coachid: String
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mainActivity = (activity as? MainActivity)!!
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.fullname.text = mainActivity?.userAdmin?.firstname + "  "+  mainActivity?.userAdmin?.lastname
        binding.firstnameinfo.text = mainActivity?.userAdmin?.firstname
        binding.lastnameinfo.text = mainActivity?.userAdmin?.lastname
        binding.emailinfo.text = mainActivity?.userAdmin?.email
        binding.dateofbirthinfo.text = mainActivity?.userAdmin?.dateOfBirth


        binding.editfirstname.setText(mainActivity?.userAdmin?.firstname)
        binding.editlastname.setText(mainActivity?.userAdmin?.lastname)
        binding.editemail.setText(mainActivity?.userAdmin?.email)
        binding.editdateofbirth.setText(mainActivity?.userAdmin?.dateOfBirth)


        return binding.root
    }
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.modifierbt.setOnClickListener {

            binding.firstnameinfo.visibility = View.GONE
            binding.lastnameinfo.visibility = View.GONE
            binding.emailinfo.visibility = View.GONE
            binding.dateofbirthinfo.visibility = View.GONE
            binding.editfirstname.visibility = View.VISIBLE
            binding.editlastname.visibility = View.VISIBLE
            binding.editemail.visibility = View.VISIBLE
            binding.editdateofbirth.visibility = View.VISIBLE
            binding.modifierbt.visibility = View.GONE
            binding.updateconfirmer.visibility = View.VISIBLE
        }
        binding.updateconfirmer.setOnClickListener {
            lifecycleScope.launch {

                try {
                    mainActivity?.userAdmin?.firstname = binding.editfirstname.text.toString()
                    mainActivity?.userAdmin?.lastname = binding.editlastname.text.toString()
                    mainActivity?.userAdmin?.email = binding.editemail.text.toString()
                    mainActivity?.userAdmin?.dateOfBirth = binding.editdateofbirth.text.toString();
                    mainActivity?.userAdmin = updateViewModel.modifyUser(mainActivity?.userAdmin!!)
                    binding.fullname.text = mainActivity?.userAdmin?.firstname + "  "+  mainActivity?.userAdmin?.lastname
                    binding.firstnameinfo.text = mainActivity?.userAdmin?.firstname
                    binding.lastnameinfo.text = mainActivity?.userAdmin?.lastname
                    binding.emailinfo.text = mainActivity?.userAdmin?.email
                    binding.dateofbirthinfo.text = mainActivity?.userAdmin?.dateOfBirth


                    binding.editfirstname.setText(mainActivity?.userAdmin?.firstname)
                    binding.editlastname.setText(mainActivity?.userAdmin?.lastname)
                    binding.editemail.setText(mainActivity?.userAdmin?.email)
                    binding.editdateofbirth.setText(mainActivity?.userAdmin?.dateOfBirth)
                    binding.firstnameinfo.visibility = View.VISIBLE
                    binding.lastnameinfo.visibility = View.VISIBLE
                    binding.emailinfo.visibility = View.VISIBLE
                    binding.dateofbirthinfo.visibility = View.VISIBLE
                    binding.editfirstname.visibility = View.GONE
                    binding.editlastname.visibility = View.GONE
                    binding.editemail.visibility = View.GONE
                    binding.editdateofbirth.visibility = View.GONE
                    binding.modifierbt.visibility = View.VISIBLE
                    binding.updateconfirmer.visibility = View.GONE
                } catch (e: Exception) {
                    // Handle exceptions
                    e.printStackTrace()
                }
            }

        }
    }


}