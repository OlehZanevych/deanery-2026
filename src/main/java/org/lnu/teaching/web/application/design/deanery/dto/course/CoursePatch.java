package org.lnu.teaching.web.application.design.deanery.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class CoursePatch {

    private String name;
    private Integer credits;
    private Integer hours;
    private Integer semester;
    private Long curriculumId;
    private Long lecturerId;
    private String info;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean empty = true;

    @JsonIgnore @Setter(AccessLevel.NONE) private boolean nameUpdated;
    @JsonIgnore @Setter(AccessLevel.NONE) private boolean creditsUpdated;
    @JsonIgnore @Setter(AccessLevel.NONE) private boolean hoursUpdated;
    @JsonIgnore @Setter(AccessLevel.NONE) private boolean semesterUpdated;
    @JsonIgnore @Setter(AccessLevel.NONE) private boolean curriculumIdUpdated;
    @JsonIgnore @Setter(AccessLevel.NONE) private boolean lecturerIdUpdated;
    @JsonIgnore @Setter(AccessLevel.NONE) private boolean infoUpdated;

    public void setName(String name)           { empty = false; nameUpdated = true;         this.name = name; }
    public void setCredits(Integer credits)    { empty = false; creditsUpdated = true;     this.credits = credits; }
    public void setHours(Integer hours)        { empty = false; hoursUpdated = true;       this.hours = hours; }
    public void setSemester(Integer semester)  { empty = false; semesterUpdated = true;   this.semester = semester; }
    public void setCurriculumId(Long id)       { empty = false; curriculumIdUpdated = true; this.curriculumId = id; }
    public void setLecturerId(Long id)         { empty = false; lecturerIdUpdated = true;   this.lecturerId = id; }
    public void setInfo(String info)           { empty = false; infoUpdated = true;         this.info = info; }
}
