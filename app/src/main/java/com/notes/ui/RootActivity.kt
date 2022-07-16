package com.notes.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.notes.R
import com.notes.databinding.ActivityRootBinding
import com.notes.ui._base.FragmentNavigator
import com.notes.ui.list.NoteListFragment

class RootActivity : AppCompatActivity(), FragmentNavigator {

    private var viewBinding: ActivityRootBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityRootBinding.inflate(layoutInflater)
        this.viewBinding = viewBinding
        setContentView(viewBinding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    viewBinding.container.id,
                    NoteListFragment()
                )
                .commit()
        } else {
            supportFragmentManager
                .findFragmentById(R.id.note_list_fragment)
        }
    }

    override fun navigateTo(
        fragment: Fragment,
        id: Long?
    ) {
        val viewBinding = this.viewBinding ?: return
        if (id != null) {
            val bundle = Bundle()
            bundle.putLong(noteId, id)
            fragment.arguments = bundle
        }
        supportFragmentManager
            .beginTransaction()
            .replace(
                viewBinding.container.id,
                fragment
            )
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val noteId = "NOTE_ID"
    }

}