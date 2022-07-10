package com.bortxapps.thewise.domain.contrats.repository

import com.bortxapps.thewise.domain.model.ConditionInOptionEntity
import com.bortxapps.thewise.domain.model.OptionEntity

interface IConditionInOptionsRepository {

    suspend fun assignOptionToCondition(conditionInOption: ConditionInOptionEntity)

    suspend fun removeOptionToCondition(conditionInOption: ConditionInOptionEntity)
}