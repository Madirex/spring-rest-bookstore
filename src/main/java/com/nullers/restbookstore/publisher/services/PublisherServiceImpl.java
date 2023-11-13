package com.nullers.restbookstore.publisher.services;

import com.nullers.restbookstore.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.publisher.dto.PublisherDto;
import com.nullers.restbookstore.publisher.exceptions.PublisherNotFound;
import com.nullers.restbookstore.publisher.mappers.CreatePublisherMapper;
import com.nullers.restbookstore.publisher.mappers.PublisherMapper;
import com.nullers.restbookstore.publisher.models.Book;
import com.nullers.restbookstore.publisher.repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Clase PublisherServiceImpl
 *
 * @author jaimesalcedo1
 * */
@Service
public class PublisherServiceImpl implements PublisherService{
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, BookRepository bookRepository){
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }
    @Override
    public List<PublisherDto> findAll() {
        return publisherRepository
                .findAll()
                .stream()
                .map(PublisherMapper::toDto).toList();
    }

    @Override
    public PublisherDto findById(UUID id) {
        return publisherRepository.findById(id)
                .map(PublisherMapper::toDto)
                .orElseThrow(() -> new PublisherNotFound("id: " + id));
    }

    @Override
    public PublisherDto save(CreatePublisherDto publisher) {
        return PublisherMapper.toDto(publisherRepository.save(CreatePublisherMapper.toPublisher(publisher)));
    }

    @Override
    public PublisherDto update(UUID id, CreatePublisherDto publisher) {
        PublisherDto publisherUpdate = findById(id);
        publisherUpdate.setName(publisher.getName());
        publisherUpdate.setImage(publisher.getImage());
        return PublisherMapper.toDto(publisherRepository.save(PublisherMapper.toPublisher(publisherUpdate)));
    }

    @Override
    public PublisherDto addBookPublisher(UUID id, UUID bookId) {
        Book bookToAdd = bookRepository.getById(bookId);
        PublisherDto publisherToUpdate = findById(id);
        publisherToUpdate.getBooks().add(bookToAdd);
        return publisherToUpdate;
    }

    @Override
    public PublisherDto removeBookPublisher(UUID id, UUID bookId) {
        Book bookToRemove = bookRepository.getById(bookId);
        PublisherDto publisherUpdate = findById(id);
        publisherUpdate.getBooks().remove(bookToRemove);
        return publisherUpdate;
    }

    @Override
    public void deleteById(UUID id) {
        PublisherDto publisherDelete = findById(id);
        publisherRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        publisherRepository.deleteAll();
    }
}
