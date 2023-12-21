package com.example.tiket2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tiket2.databinding.FragmentBookmarkBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var prefManager: PrefManager
    private val movieCollectionRef = FirebaseFirestore.getInstance().collection("Movies")
    private val usersCollectionRef = FirebaseFirestore.getInstance().collection("Users")
    private var movieList = ArrayList<MovieData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookmarkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookmarkBinding.bind(view)


        // Inisialisasi prefManager saat Fragment terhubung dengan Activity
        prefManager = PrefManager.getInstance(requireContext())

        initVars()
        getImages()

        with(binding) {
            movieAdapter.onItemClick = {
                startActivity(Intent(activity, MovieDetails::class.java).apply {
                    putExtra(ListMovieFragment.EXTRA_ID, it.id)
                    putExtra(ListMovieFragment.EXTRA_IMAGE, it.gambar)
                    putExtra(ListMovieFragment.EXTRA_NAMA, it.nama)
                    putExtra(ListMovieFragment.EXTRA_DIREKTOR, it.direktor)
                    putExtra(ListMovieFragment.EXTRA_RATING, it.rating)
                    putExtra(ListMovieFragment.EXTRA_STORY, it.storyline)
                    putExtra(ListMovieFragment.EXTRA_GENRE, it.genre.toTypedArray())
                })
            }
        }
    }

    private fun initVars() {
        binding.layoutRV.setHasFixedSize(true)
        binding.layoutRV.layoutManager = GridLayoutManager(activity, 2)
        movieAdapter = MovieAdapter(movieList)
        binding.layoutRV.adapter = movieAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImages() {
        usersCollectionRef.document(prefManager.getUsername()).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedBookmark = document.get("bookmark") as MutableList<String>? ?: mutableListOf()

                    // Dapatkan daftar ID film dari koleksi
                    movieCollectionRef.get()
                        .addOnSuccessListener { querySnapshotMovies ->
                            // Filter nilai favorit yang tidak ada di koleksi film
                            val invalidFavorites = storedBookmark.filter { id ->
                                // Filter ID yang tidak ada di koleksi film
                                !querySnapshotMovies.documents.any { it.id == id }
                            }

                            // Jika ada nilai favorit yang tidak valid, hapus dari storedFavorite
                            if (invalidFavorites.isNotEmpty()) {
                                storedBookmark.removeAll(invalidFavorites)

                                // Perbarui nilai "favorite" di usersCollectionRef menggunakan fungsi updateFavorite()
                                updateBookmark(storedBookmark)
                            }

                            // Ambil data film untuk nilai favorit yang valid
                            movieCollectionRef.whereIn(FieldPath.documentId(), storedBookmark).get()
                                .addOnSuccessListener { querySnapshot ->
                                    movieList.clear()
                                    for (document in querySnapshot.documents) {
                                        val movieData = MovieData(
                                            document.id,
                                            document.getString("gambar") ?: "",
                                            document.getString("nama") ?: "",
                                            document.getString("rating") ?: "",
                                            document.getString("direktor") ?: "",
                                            document.get("genre") as List<String>? ?: listOf(),
                                            document.getString("storyline") ?: ""
                                        )
                                        movieList.add(movieData)
                                    }
                                    movieAdapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("BookmarkFragment", "Error fetching movie data", exception)
                                    Toast.makeText(activity, "Error fetching movie data", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("BookmarkFragment", "Error fetching movie collection data", exception)
                            Toast.makeText(activity, "Error fetching movie collection data", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("BookmarkFragment", "Tidak ada koneksi internet", exception)
                Toast.makeText(activity, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateBookmark(updatedBookmark: List<String>) {
        val mapDocument = HashMap<String, Any>()
        mapDocument["bookmark"] = updatedBookmark

        usersCollectionRef.document(prefManager.getUsername()).update(mapDocument)
            .addOnSuccessListener {
                Log.d("BookmarkFragment", "Updated favorite successfully")
            }
            .addOnFailureListener { exception ->
                Log.d("BookmarkFragment", "Error updating favorite", exception)
                Toast.makeText(activity, "Error updating favorite", Toast.LENGTH_SHORT).show()
            }
    }
}