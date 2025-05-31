package org.tasks.myshop.enums;

import lombok.Getter;
import org.tasks.myshop.exception.SortException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum SortEnum {

    NO("NO", "id"),
    ALPHA("ALPHA", "title"),
    PRICE("PRICE", "price"),
    ;

    private final String value;
    private final String sortField;

    SortEnum(String value, String sortField) {
        this.value = value;
        this.sortField = sortField;
    }

    private static final Map<String, SortEnum> ALL_VALUES_MAP = Arrays.stream(SortEnum.values()).collect(Collectors.toMap(SortEnum::getValue, Function.identity()));

    public static SortEnum getByValue(String value) throws SortException {
        return Optional.ofNullable(ALL_VALUES_MAP.get(value))
                .orElseThrow(() -> new SortException("Сортировка" + value + " отсутствует в списке возможных"));
    }

}
