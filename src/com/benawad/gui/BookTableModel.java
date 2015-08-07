package com.benawad.gui;

import com.benawad.models.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by benawad on 8/5/15.
 */
public class BookTableModel extends AbstractTableModel {

    private static final int TITLE_COL = 0;
    private static final int AUTHORS_COL = 1;

    private String[] columnNames = { "Title", "Authors" };
    private List<Book> books;

    public BookTableModel(List<Book> bookList){
        books = bookList;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);

        switch (columnIndex){
            case TITLE_COL:
                return book.getTitle();
            case AUTHORS_COL:
                return Book.authorsToString(book.getAuthors());
            default:
                return book.getTitle();
        }
    }

    @Override
    public Class getColumnClass(int c){
        return getValueAt(0, c).getClass();
    }
}
