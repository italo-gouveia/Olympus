package com.olympus.data.vo;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

//Relation defines name of collections in return of hateoas json
@Relation(collectionRelation = "todos")
@JsonPropertyOrder({ "id", "name", "description"})
public class TodoVO extends RepresentationModel<TodoVO> implements Serializable  {
    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;
    private String name;
    private String description;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoVO)) return false;
        if (!super.equals(o)) return false;
        TodoVO todoVO = (TodoVO) o;
        return key.equals(todoVO.key) &&
                name.equals(todoVO.name) &&
                description.equals(todoVO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, name, description);
    }

}
