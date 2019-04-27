package com.obsez.mobile.leshananim

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.view.*

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ItemListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [ItemListDialogFragment.Listener].
 */
class ItemListDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        list.adapter = ItemAdapter(arguments?.getInt(ARG_ITEM_COUNT)!!)
    }
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener = if (parent != null) {
            parent as Listener
        } else {
            context as Listener
        }
    }
    
    override fun onDetach() {
        mListener = null
        super.onDetach()
    }
    
    interface Listener {
        fun onItemClicked(position: Int)
    }
    
    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false)) {
        
        internal val text: TextView = itemView.text
        
        init {
            text.setOnClickListener {
                mListener?.let {
                    it.onItemClicked(adapterPosition)
                    dismiss()
                }
            }
        }
    }
    
    private inner class ItemAdapter internal constructor(private val mItemCount: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }
        
        override fun getItemCount(): Int {
            return mItemCount
        }
    }
    
    companion object {
        
        // TODO: Customize parameters
        fun newInstance(itemCount: Int): ItemListDialogFragment =
            ItemListDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }
        
    }
}
