package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class DataRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): DataDao?
    //memberikan akses ke objek NoteDao untuk melakukan operasi database terkait entitas Note.

    companion object {
        @Volatile
        private var INSTANCE: DataRoomDatabase? = null
        fun getDatabase(context: Context): DataRoomDatabase? {
            //membuat dan mengembalikan instance dari NoteRoomDatabase.
            if (INSTANCE == null) {
                synchronized(DataRoomDatabase::class.java) {
                    //dibuat menggunakan databaseBuilder. Nama database menjadi "note_database".
                    // jika INSTANCE ada, maka instance yang ada akan digunakan.
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        DataRoomDatabase::class.java, "note_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}