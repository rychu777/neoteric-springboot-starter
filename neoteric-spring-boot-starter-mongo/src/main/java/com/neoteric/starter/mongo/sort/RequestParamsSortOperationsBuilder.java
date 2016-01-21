package com.neoteric.starter.mongo.sort;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.neoteric.request.sort.RequestSort;
import com.neoteric.request.sort.SortType;
import com.neoteric.starter.mongo.request.FieldMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.SortOperation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class RequestParamsSortOperationsBuilder {

    public static RequestParamsSortOperationsBuilder newBuilder() {
        return new RequestParamsSortOperationsBuilder();
    }

    private static final Map<SortType, Sort.Direction> SORT_MAPPING = ImmutableMap.of(
            SortType.ASC, Sort.Direction.ASC,
            SortType.DESC, Sort.Direction.DESC
    );

    public Optional<SortOperation> build(List<RequestSort> requestSortList) {
        return build(requestSortList, FieldMapper.of(Maps.<String, String>newHashMap()));
    }

    public Optional<SortOperation> build(List<RequestSort> requestSortList, FieldMapper fieldMapper) {
        Iterator<RequestSort> iterator = requestSortList.iterator();
        SortOperation sortOperation = null;
        if (iterator.hasNext()) {
            RequestSort requestSort = iterator.next();
            sortOperation = sort(SORT_MAPPING.get(requestSort.getType()), fieldMapper.get(requestSort.getFieldName()));
        }
        while (iterator.hasNext()) {
            RequestSort requestSort = iterator.next();
            sortOperation = sortOperation.and(SORT_MAPPING.get(requestSort.getType()), fieldMapper.get(requestSort.getFieldName()));
        }
        return Optional.ofNullable(sortOperation);
    }
}
