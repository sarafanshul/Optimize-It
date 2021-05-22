package com.projectdelta.optimize.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.optimize.R
import com.projectdelta.optimize.adapter.RecyclerViewLoadProjectAdapter
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.databinding.LoadProjectFragmentBinding
import com.projectdelta.optimize.viewModel.LoadProjectViewModel

class LoadProjectFragment : Fragment() {

	companion object {
		fun newInstance() = LoadProjectFragment()
	}

	private lateinit var viewModel: LoadProjectViewModel
	lateinit var adapter : RecyclerViewLoadProjectAdapter
	private var _binding : LoadProjectFragmentBinding? = null
	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = LoadProjectFragmentBinding.inflate(inflater , container , false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this).get(LoadProjectViewModel::class.java)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val DB = ProjectDatabase.getInstance(context?.applicationContext!!).projectDao()

		binding.loadProjectRvMain.layoutManager = LinearLayoutManager(activity)
		adapter = RecyclerViewLoadProjectAdapter()
		binding.loadProjectRvMain.adapter = adapter

		DB.getAllProjects().observe(viewLifecycleOwner , { data ->
			adapter.set( data )
		})
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}