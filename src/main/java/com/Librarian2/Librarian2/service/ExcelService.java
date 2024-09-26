package com.Librarian2.Librarian2.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Librarian2.Librarian2.dao.BookDao;
import com.Librarian2.Librarian2.helper.Helper;
import com.Librarian2.Librarian2.models.Books;

@Service
public class ExcelService {
	@Autowired
    private BookDao bookDao;

    public ByteArrayInputStream getActualData() throws IOException{
        List<Books> all = bookDao.findAvailableBooks();
        ByteArrayInputStream byteArrayInputStream = Helper.dataToExcel(all);
        return byteArrayInputStream;
    }
}
