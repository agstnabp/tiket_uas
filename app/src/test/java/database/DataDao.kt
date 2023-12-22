package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DataDao {
    //membuat beberapa fungsi
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Data)

    @Update
    fun update(note: Data)

    @Delete
    fun delete(note: Data)

    //get all notes
    @get:Query("SELECT * from data_table ORDER BY id ASC")
    val allNotes: LiveData<List<Data>>

    //delete oleh id
    @Query("DELETE FROM data_table WHERE id = :noteId")
    fun deleteById(noteId: Int)
}