package com.notes.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.details.NoteDetailsFragment

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    private val viewModel: NoteListViewModel by viewModels { DependencyManager.noteListViewModelFactory() }

    private val recyclerViewAdapter = RecyclerViewAdapter(
        onViewClick = {
            findImplementationOrThrow<FragmentNavigator>()
                .navigateTo(
                    NoteDetailsFragment(),
                    it
                )
        }
    )

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
        }

        viewModel.notes.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                recyclerViewAdapter.setItems(it.sortedWith(compareBy<NoteListItem>({ it.modifiedAt }).reversed()))
            }
        }
        viewModel.navigateToNoteCreation.observe(
            viewLifecycleOwner
        ) {
            findImplementationOrThrow<FragmentNavigator>()
                .navigateTo(
                    NoteDetailsFragment(),
                    null
                )
        }
    }

    private class RecyclerViewAdapter(
        private val onViewClick: (Long) -> Unit
    ) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onViewClick
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size

        fun setItems(
            items: List<NoteListItem>
        ) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        inner class ViewHolder(
            private val binding: ListItemNoteBinding,
            private val onViewClick: (Long) -> Unit
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(
                note: NoteListItem
            ) {
                with(binding) {
                    binding.titleLabel.text = note.title
                    binding.contentLabel.text = note.content

                    root.setOnClickListener {
                        onViewClick.invoke(note.id)
                    }
                }
            }
        }
    }

}