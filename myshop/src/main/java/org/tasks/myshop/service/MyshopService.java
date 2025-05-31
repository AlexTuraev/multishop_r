package org.tasks.myshop.service;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.model.ItemModel;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.enums.SortEnum;
import org.tasks.myshop.exception.LoadItemException;
import org.tasks.myshop.exception.SortException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MyshopService {

    Mono<List<ItemEntity>> getItems(String search, Integer pageSize, Integer pageNumber, SortEnum sortType);

    Flux<ItemModel> getItemsOverMinQuantity(String search, Integer pageSize, Integer pageNumber, SortEnum sortType, int minQuantity);

    Mono<Model> getItemsModel(Model model, String search, Integer pageSize, Integer pageNumber, String sort) throws SortException;

    Mono<Void> loadItemsFromCsv(MultipartFile file, MultipartFile[] images) throws LoadItemException;

    Mono<ItemDto> getItemById(Long id);

    Mono<Model> changeItemCart(Model model, Long itemId, Long cartId, int delta);

    Mono<ItemEntity> updateCountItem(Long itemId, int deltaCount);

}
