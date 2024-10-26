package com.example.resumaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResumeData(
    val personalDetails: List<PersonalDetail> = emptyList(),
    var education: MutableList<Education> = mutableListOf(),
    var experience: MutableList<Experience> = mutableListOf(),
    var skills: MutableList<Skill> = mutableListOf(),
    var objectives: MutableList<Objective> = mutableListOf(),
    var projects: MutableList<Project> = mutableListOf(),
    var awards: MutableList<Award> = mutableListOf()
) : Parcelable

@Parcelize
data class PersonalDetail(
    val name: String,
    val desiredJob: String,
    val address: String,
    val email: String,
    val phone: String,
    val linkedIn: String,
) : Parcelable

@Parcelize
data class Education(
    val program: String,
    val school: String,
    val schoolYear: String
) : Parcelable

@Parcelize
data class Experience(
    val company: String,
    val jobTitle: String,
    val startDate: String,
    val endDate: String,
    val details: String
) : Parcelable

@Parcelize
data class Skill(
    val skillName: String
) : Parcelable

@Parcelize
data class Objective(
    val objectiveText: String
) : Parcelable

@Parcelize
data class Project(
    val projectTitle: String,
    val projectDescription: String
) : Parcelable

@Parcelize
data class Award(
    val award: String,
    val awardDescription: String
) : Parcelable
