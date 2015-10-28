package com.neoteric.starter.request;

import com.neoteric.request.LogicalOperatorType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalOperatorTypeTest {


    @Test
    public void shouldReturnTrueForOrOperator() throws Exception {
        assertThat(LogicalOperatorType.contains("$or")).isTrue();
    }

    @Test
    public void shouldReturnFalseForUnknownOperator() throws Exception {
        assertThat(LogicalOperatorType.contains("$tor")).isFalse();
    }
}