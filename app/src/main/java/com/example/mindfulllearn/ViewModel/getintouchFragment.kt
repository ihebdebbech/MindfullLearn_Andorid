package com.example.mindfulllearn.ViewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.User
import com.example.mindfulllearn.R
import com.example.mindfulllearn.databinding.FragmentGetintouchBinding
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [getintouchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class getintouchFragment : Fragment(R.layout.fragment_getintouch) {
    // TODO: Rename and change types of parameters
    private lateinit var binding : FragmentGetintouchBinding
    private lateinit var coachid:String
    var userCoach : User?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle = arguments
        val markerTitle = bundle?.getString("markerTitle")
        coachid = bundle?.getString("coachid").toString()
        // Inflate the layout for this fragment
        binding = FragmentGetintouchBinding.inflate(inflater, container, false)
        binding.name.text=markerTitle


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.getintouchbutton.setOnClickListener {
            val mainActivity = activity as? MainActivity
           // val fragmentManager = parentFragmentManager.findFragmentById(R.id.fragmentContainerView)
            val bundle1 = Bundle()
            val newFragment =BlankFragment()
            newFragment.arguments = bundle1
            bundle1.putString("markerTitle", binding.name.text.toString())
            bundle1.putString("coachid", coachid)
            mainActivity?.supportFragmentManager?.beginTransaction()?.apply {
                replace( R.id.fragmentContainerView,newFragment)
              addToBackStack("chat")
               commit()
            }
            // Handle backstack button click action (if needed)
            // For example: Pop the fragment backstack here
           /* val transaction = parentFragmentManager.beginTransaction()
            transaction.replace( R.id.fragmentContainerView,newFragment)
            transaction.addToBackStack(null)
            transaction.commit()*/
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment getintouchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            getintouchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}