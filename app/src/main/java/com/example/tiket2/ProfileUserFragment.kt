package com.example.tiket2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tiket2.HomeUserActivity
import com.example.tiket2.ListMovieFragment
import com.example.tiket2.PrefManager
import com.example.tiket2.R
import com.example.tiket2.databinding.FragmentProfileUserBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentProfileUserBinding
    private lateinit var homeUserActivity: HomeUserActivity
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_user, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Inisialisasi homeUserActivity saat Fragment terhubung dengan Activity
        if (context is HomeUserActivity) {
            homeUserActivity = context
        } else {
            throw IllegalStateException("Activity harus merupakan instance dari HomeUserActivity")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileUserBinding.bind(view)

        // Inisialisasi prefManager saat Fragment terhubung dengan Activity
        prefManager = PrefManager.getInstance(requireContext())

        var username = prefManager.getUsername()
        var email = prefManager.getEmail()


        with(binding) {
            txtUser.setText(username)
            txtEmail.setText(email)

            btnLogout.setOnClickListener {
                // Call the logOut() method in HomeUserActivity
                homeUserActivity.logOut()

                // Display a toast message
                showToast("Logout successful")

            }
        }
    }
    // Function to show a toast message
    private fun showToast(message: String) {
        val context: Context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}