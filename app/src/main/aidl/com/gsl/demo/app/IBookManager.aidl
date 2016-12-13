// IBookManager.aidl
package com.gsl.demo.app;

import com.gsl.demo.app.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
