package com.notes.ui.details

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.notes.R
import com.notes.data.NoteDbo
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.list.NoteListFragment
import java.time.LocalDateTime


class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {
    private val viewModel: NoteDetailsViewModel by viewModels {
        DependencyManager.noteDetailViewModelFactory()
    }

    private var noteId: Long? = null
    private var noteDbo: NoteDbo? = null


    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)


        arguments?.let {
            noteId = it.getLong(noteIdArg)
            viewModel.getNote(noteId!!)
        }

        viewModel.note.observe(viewLifecycleOwner) {
            noteDbo = it
            with(viewBinding) {
                etTitle.setText(it.title)
                etContent.setText(it.content)
            }
        }
        viewBinding.fabDelete.setOnClickListener {
            noteDbo?.let {
                viewModel.deleteNote(it)
                navigateToBack()
            }
        }
        viewBinding.toolbar.setNavigationOnClickListener {
            with(viewBinding) {
                when (noteDbo) {
                    null -> {
                        if (etTitle.text.isNotEmpty()) {
                            viewModel.saveNote(
                                this.etTitle.text.toString(),
                                this.etContent.text.toString()
                            )
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.fillTitleText),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else -> {
                        viewModel.updateNote(
                            noteDbo!!.copy(
                                title = this.etTitle.text.toString(),
                                content = this.etContent.text.toString(),
                                modifiedAt = LocalDateTime.now()
                            )
                        )
                    }
                }

            }
            navigateToBack()
        }
    }

    private fun navigateToBack() {
        findImplementationOrThrow<FragmentNavigator>()
            .navigateTo(
                NoteListFragment(),
                null
            )
    }

    companion object {
        const val noteIdArg = "NOTE_ID"
    }
}