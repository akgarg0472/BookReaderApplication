package com.akgarg.bookreader.book;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface BookRepository extends CassandraRepository<Book, String> {
}
