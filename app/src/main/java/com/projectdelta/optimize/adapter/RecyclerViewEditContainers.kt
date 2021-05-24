package com.projectdelta.optimize.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.projectdelta.optimize.R
import com.projectdelta.optimize.data.entities.Container

class RecyclerViewEditContainers( private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	lateinit var data : List<Container>

	inner class ContainerViewHolder( itemView : View ) : RecyclerView.ViewHolder(itemView){
		fun bind( position: Int ){
			itemView.findViewById<MaterialTextView>(R.id.edit_container_tw_name).text = data[position].containerName
			itemView.findViewById<MaterialTextView>(R.id.edit_container_tw_value).text = data[position].value.toString()
			itemView.findViewById<MaterialTextView>(R.id.edit_container_tw_weight).text = data[position].weight.toString()
			itemView.findViewById<MaterialTextView>(R.id.edit_container_tw_count).text = data[position].count.toString()
		}
	}

	inner class PlaceholderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val btn = itemView.findViewById<MaterialCardView>(R.id.edit_container_add_cw_add)

		fun bind( position: Int ){
//			btn.setOnClickListener {
//				Toast.makeText(context , "Add clicked !" , Toast.LENGTH_LONG ).show()
//			}
			// using external adapter for this
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when( viewType ){
			R.layout.layout_rv_edit_containers -> {
				val view = LayoutInflater.from( context ).inflate( R.layout.layout_rv_edit_containers ,parent , false )
				ContainerViewHolder(view)
			}
			R.layout.layout_rv_edit_containers_add -> {
				val view = LayoutInflater.from( context ).inflate( R.layout.layout_rv_edit_containers_add ,parent , false )
				PlaceholderViewHolder( view )
			}
			else -> throw IllegalArgumentException("Unknown view type $viewType")
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when( getItemViewType( position ) ){
			R.layout.layout_rv_edit_containers -> (holder as ContainerViewHolder).bind( position )
			R.layout.layout_rv_edit_containers_add -> (holder as PlaceholderViewHolder).bind( position )
		}
	}

	override fun getItemCount(): Int {
		if( this::data.isInitialized ) return data.size + 1
		return 1
	}

	override fun getItemViewType(position: Int): Int {
		if( this::data.isInitialized && position == data.size || ! this::data.isInitialized )
			return R.layout.layout_rv_edit_containers_add
		return R.layout.layout_rv_edit_containers

	}

	fun set( _data : List<Container> ){
		data = _data
		notifyDataSetChanged()
	}
}