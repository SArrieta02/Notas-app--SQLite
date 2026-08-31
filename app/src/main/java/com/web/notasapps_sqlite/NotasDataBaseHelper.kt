package com.web.notasapps_sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotasDataBaseHelper (context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME($COLUM_ID INTEGER PRIMARY KEY, $COLUM_TITLE TEXT, $COLUM_DESCRIPTION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldversion: Int, newVersion: Int) {
        val dropTableQuery =
            "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "notas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notas"
        private const val COLUM_ID = "id"
        private const val COLUM_TITLE = "titulo"
        private const val COLUM_DESCRIPTION = "descripcion"
    }

    fun insertNota(nota: Nota) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUM_TITLE, nota.titulo)
            put(COLUM_DESCRIPTION, nota.descripcion)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotas(): List<Nota> {
        val listaNotas = mutableListOf<Nota>()
        val db = readableDatabase
        val query = "SELECT * FROM  $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUM_ID))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_TITLE))
            val descricion = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_DESCRIPTION))

            val nota= Nota (id, titulo, descricion)
            listaNotas.add(nota)
        }
        cursor.close()
        db.close()
        return listaNotas
    }

    fun getIdNota(idNota: Int): Nota{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUM_ID = $idNota"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUM_ID))
        val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_TITLE))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_DESCRIPTION))

        cursor.close()
        db.close()

        return Nota(id,titulo,descripcion)
    }

    fun updateNota(nota: Nota){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUM_TITLE, nota.id)
            put(COLUM_DESCRIPTION, nota.descripcion)
        }

        val whereClause = "$COLUM_ID = ?"
        val whereArgs = arrayOf(nota.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }


}