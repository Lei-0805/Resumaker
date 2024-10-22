import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResumeData(
    val personalDetails: List<PersonalDetail>,
    val education: List<Education>,
    val experience: List<Experience>,
    val skills: List<Skill>,
    val objective: Objective,
    val projects: List<Project>,
    val awards: List<Award>
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
    val title: String,
    val description: String
) : Parcelable

@Parcelize
data class Award(
    val achievement: String,
    val description: String
) : Parcelable
