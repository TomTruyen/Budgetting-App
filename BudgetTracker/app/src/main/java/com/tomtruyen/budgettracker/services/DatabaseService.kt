package com.tomtruyen.budgettracker.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tomtruyen.budgettracker.models.overview.Transaction
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.ArrayList

class DatabaseService(context: Context?) : SQLiteOpenHelper(context, context?.getExternalFilesDirs(null)?.last()?.absolutePath + DATABASE_NAME, null, DATABASE_VERSION) {
    private val gson: Gson = Gson()

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BudgetyDB"
        private const val TABLE_TRANSACTIONS = "TransactionTable"
        private const val KEY_ID = "id"
        private const val KEY_TRANSACTION = "transaction_item"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTransactionTable = ("CREATE TABLE $TABLE_TRANSACTIONS($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,$KEY_TRANSACTION TEXT)")
        db?.execSQL(createTransactionTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Get Current Data
        val transactions : List<Transaction> = read()

        // Drop DB
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")

        // Recreate DB
        onCreate(db)

        // Fill with previous data
        if(transactions.isNotEmpty()) {
            for(transaction in transactions) {
                save(transaction)
            }
        }
    }

    fun read() : List<Transaction> {
        val transactions = ArrayList<Transaction>()

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTIONS ORDER BY id DESC", null)

        } catch (e: SQLiteException) {
            return transactions
        }

        var id: Int
        var transactionString : String

        if(cursor.moveToFirst()) {
            do {
                try {
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    transactionString = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION))

                    val transaction = gson.fromJson(transactionString, Transaction::class.java)
                    transaction.id = id

                    transactions.add(transaction)
                } catch (e: JsonSyntaxException) {}

            } while(cursor.moveToNext())

        }
        cursor.close()

        return transactions
    }

    fun readOne(position: Int) : Transaction? {
        return try {
            read()[position]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun length() : Int {
        return read().size
    }

    fun save(transaction: Transaction) {
        try {
            val db = this.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(KEY_TRANSACTION, gson.toJson(transaction))

            // New
            if(transaction.id == -1) {
                db.insert(TABLE_TRANSACTIONS, null, contentValues)
            } else {
                db.update(TABLE_TRANSACTIONS, contentValues, "id='${transaction.id}'", null)
            }
        } catch (e: SQLiteException) {}
    }

    fun delete(position: Int) {
        try {
            val db = this.writableDatabase
            val transaction = readOne(position)

            db.delete(TABLE_TRANSACTIONS, "id='${transaction?.id}'", null)
        } catch (e: SQLiteException) {}
    }
}