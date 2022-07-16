package com.notes.di

import com.notes.ui.details.NoteDetailsViewModelFactory
import com.notes.ui.list.NoteListViewModelFactory
import dagger.Component

@RootScope
@Component(
    dependencies = [
        AppComponent::class,
    ],
    modules = [
    ]
)
interface RootComponent {

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): RootComponent
    }

    fun getNoteListViewModelFactory(): NoteListViewModelFactory
    fun getNoteDetailViewModelFactory(): NoteDetailsViewModelFactory

}