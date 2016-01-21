package com.neoteric.starter.mongo.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.neoteric.request.FiltersParser;
import com.neoteric.request.RequestObject;
import com.neoteric.request.sort.RequestSort;
import com.neoteric.request.sort.SortParser;
import com.neoteric.starter.mongo.SpringbootTestApplication;
import com.neoteric.starter.mongo.sort.RequestParamsSortOperationsBuilder;
import com.neoteric.starter.mongo.test.EmbeddedMongoTest;
import com.neoteric.starter.test.restassured.ContainerIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SpringbootTestApplication.class})
@ContainerIntegrationTest
@EmbeddedMongoTest(dropCollections = "FooModel")
public class MongoCriteriaIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(MongoCriteriaIntegrationTest.class);
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testReturnObjectBasedOnStartsWithCriteria() throws Exception {
        mongoTemplate.insert(FooModel.newBuilder().setName("Johnny").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Paul").build());

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("startsWithFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(FooModel.newBuilder().setName("Johnny").build());
    }

    @Test
    public void testReturnObjectBasedOnInCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("inFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("John", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
    }

    @Test
    public void testReturnObjectBasedOnNinCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("ninFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
    }

    @Test
    public void testReturnObjectBasedOnGtCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("gtFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
    }

    @Test
    public void testReturnObjectBasedOnGteCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("gteFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
    }

    @Test
    public void testReturnObjectBasedOnLtCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("ltFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("John", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
    }

    @Test
    public void testReturnObjectBasedOnLteCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("lteFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(3);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("John", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
        assertThat(results.get(2)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
    }

    @Test
    public void testReturnObjectBasedOnEqCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("eqFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
    }

    @Test
    public void testReturnObjectBasedOnNeqCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("John", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("neqFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("John", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
    }

    @Test
    public void testReturnObjectBasedOnNameCriteria() throws Exception {
        mongoTemplate.insert(FooModel.newBuilder().setName("Johnny").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Paul").build());

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("basicOperatorsFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(FooModel.newBuilder().setName("Johnny").build());
    }

    @Test
    public void testReturnObjectBasedOnLogicalOperatorCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("Johnny", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Paul", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Adam", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("logicalOperator.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Johnny", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("Adam", 7));
    }

    @Test
    public void testReturnObjectBasedOnAndedCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("Johnny", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("multipleRootElements.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
    }

    @Test
    public void testReturnObjectBasedOnMultipleOrCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("Johnny", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Barry", 2));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 9));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("multipleOrElements.json"));
        List<FooModel> results = performCriteriaCall(criteria);

        assertThat(results.size()).isEqualTo(4);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Johnny", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
        assertThat(results.get(2)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
        assertThat(results.get(3)).isEqualTo(FooModelMother.fullyPopulated("Bogdan", 9));
    }

    @Test
    public void testReturnObjectBasedOnArrayOperatorsCriteria() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("Johnny", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Barry", 2));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 9));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(readFiltersFromResources("basicArrayOperators.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Johnny", 1));
    }

    @Test
    public void testReturnObjectsSorted() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 9));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Jill", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Johnny", 1));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Barry", 2));

        List<FooModel> results = performCriteriaCall(new Criteria(), RequestParamsSortOperationsBuilder.newBuilder().build(readSortFromResources("sort.json")).get());
        assertThat(results.size()).isEqualTo(5);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Johnny", 1));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("Jill", 5));
        assertThat(results.get(2)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
        assertThat(results.get(3)).isEqualTo(FooModelMother.fullyPopulated("Bogdan", 9));
        assertThat(results.get(4)).isEqualTo(FooModelMother.fullyPopulated("Barry", 2));
    }

    @Test
    public void testReturnObjectsSortedByMultipleFields() throws Exception {
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 7));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 2));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 3));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 5));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 1));

        List<FooModel> results = performCriteriaCall(new Criteria(), RequestParamsSortOperationsBuilder.newBuilder().build(readSortFromResources("multipleSort.json")).get());
        assertThat(results.size()).isEqualTo(6);
        FooModel fooModel = results.get(0);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("James", 2));
        assertThat(results.get(1)).isEqualTo(FooModelMother.fullyPopulated("James", 5));
        assertThat(results.get(2)).isEqualTo(FooModelMother.fullyPopulated("James", 7));
        assertThat(results.get(3)).isEqualTo(FooModelMother.fullyPopulated("Bogdan", 1));
        assertThat(results.get(4)).isEqualTo(FooModelMother.fullyPopulated("Bogdan", 3));
        assertThat(results.get(5)).isEqualTo(FooModelMother.fullyPopulated("Bogdan", 5));
    }

    @Test
    public void testReturnObjectsBasedOnFiltersWithAdditionalCriteria() throws Exception {
        mongoTemplate.insert(FooModel.newBuilder().setName("Johnny").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("James").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Julian").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Bob").build());

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(
                Optional.of(Criteria.where("name").in(Lists.newArrayList("James", "Julian"))), readFiltersFromResources("startsWithJFilters.json"));
        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModel.newBuilder().setName("James").build());
        assertThat(results.get(1)).isEqualTo(FooModel.newBuilder().setName("Julian").build());
    }

    @Test
    public void testReturnObjectsAdditionalCriteriaThatAreNotRemapped() throws Exception {
        mongoTemplate.insert(FooModel.newBuilder().setName("Johnny").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("James").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Julian").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Bob").build());

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(
                Optional.of(Criteria.where("name").in(Lists.newArrayList("James", "Julian"))),
                readFiltersFromResources("emptyFilters.json"),
                FieldMapper.of(ImmutableMap.of("apiName", "name")));

        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(FooModel.newBuilder().setName("James").build());
        assertThat(results.get(1)).isEqualTo(FooModel.newBuilder().setName("Julian").build());
    }

    @Test
    public void testReturnNoObjectsDueToFactThatAdditionalCriteriaShouldContainRemappedValues() throws Exception {
        mongoTemplate.insert(FooModel.newBuilder().setName("Johnny").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("James").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Julian").build());
        mongoTemplate.insert(FooModel.newBuilder().setName("Bob").build());

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(
                Optional.of(Criteria.where("apiName").in(Lists.newArrayList("James", "Julian"))),
                readFiltersFromResources("emptyFilters.json"),
                FieldMapper.of(ImmutableMap.of("apiName", "name")));

        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(0);
    }
/*
    @Test
    public void testReturnObjectsBasedOnDateParameters() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();
        mongoTemplate.insert(FooModelMother.fullyPopulated("Johnny", 7, now));
        mongoTemplate.insert(FooModelMother.fullyPopulated("James", 2, now.minusDays(1)));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Julian", 5, now.minusDays(2)));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Adam", 3, now.plusDays(1)));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bob", 5, now.plusDays(2)));
        mongoTemplate.insert(FooModelMother.fullyPopulated("Bogdan", 1, now.plusDays(3)));

        Criteria criteria = RequestParamsCriteriaBuilder.newBuilder().build(
                Optional.of(Criteria.where("date").gt(now)),
                readFiltersFromResources("emptyFilters.json"));

        List<FooModel> results = performCriteriaCall(criteria);
        assertThat(results.size()).isEqualTo(3);
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Adam", 3, now.plusDays(1)));
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Bob", 5, now.plusDays(2)));
        assertThat(results.get(0)).isEqualTo(FooModelMother.fullyPopulated("Bogdan", 1, now.plusDays(3)));
    }
*/
    private List<FooModel> performCriteriaCall(Criteria criteria) {
        TypedAggregation<FooModel> aggregation = newAggregation(FooModel.class, match(criteria));
        AggregationResults<FooModel> aggregationResults = mongoTemplate.aggregate(aggregation, FooModel.class);
        return Lists.newArrayList(aggregationResults.iterator());
    }

    private List<FooModel> performCriteriaCall(Criteria criteria, SortOperation sortOperation) {
        TypedAggregation<FooModel> aggregation = newAggregation(FooModel.class, match(criteria), sortOperation);
        AggregationResults<FooModel> aggregationResults = mongoTemplate.aggregate(aggregation, FooModel.class);
        return Lists.newArrayList(aggregationResults.iterator());
    }

    private Map<RequestObject, Object> readFiltersFromResources(String resourceName) throws IOException {
        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/criteria-tests/" + resourceName));
        ObjectMapper mapper = new ObjectMapper();
        return FiltersParser.parseFilters(mapper.readValue(jsonBytes, Map.class));
    }

    private List<RequestSort> readSortFromResources(String resourceName) throws IOException {
        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/criteria-tests/" + resourceName));
        ObjectMapper mapper = new ObjectMapper();
        return SortParser.parseSort(mapper.readValue(jsonBytes, Map.class));
    }
}