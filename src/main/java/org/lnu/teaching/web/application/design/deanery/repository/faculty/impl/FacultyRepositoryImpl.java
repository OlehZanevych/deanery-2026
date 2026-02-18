package org.lnu.teaching.web.application.design.deanery.repository.faculty.impl;

import lombok.AllArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyPatch;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.query.params.FacultyFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.repository.faculty.FacultyRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class FacultyRepositoryImpl implements FacultyRepository {

    private static final String INSERT_FACULTY_QUERY = """
            INSERT INTO faculties (
                name,
                website,
                email,
                phone,
                address,
                info
            ) VALUES (
                :name,
                :website,
                :email,
                :phone,
                :address,
                :info
            )
            """;

    private static final String SELECT_FACULTIES_QUERY = """
            SELECT
                id,
                name,
                website,
                email,
                phone,
                address,
                info
            FROM faculties
            """;

    private static final String SELECT_FACULTY_COUNT_QUERY = """
            SELECT COUNT(1) FROM faculties
            """;

    private static final String SELECT_FACULTY_BY_ID_QUERY = """
            SELECT
                id,
                name,
                website,
                email,
                phone,
                address,
                info
            FROM faculties
            WHERE id = :id
            """;

    private static final String UPDATE_FACULTY_BY_ID_QUERY = """
            UPDATE faculties SET
                name = :name,
                website = :website,
                email = :email,
                phone = :phone,
                address = :address,
                info = :info
            WHERE id = :id
            """;

    private static final String PATCH_FACULTY_BY_ID_QUERY_TEMPLATE = """
            UPDATE faculties SET
                %s
            WHERE id = :id
            """;

    private static final String DELETE_FACULTY_BY_ID_QUERY = """
            DELETE FROM faculties WHERE id = :id
            """;

    private static final RowMapper<FacultyEntity> FACULTY_ROW_MAPPER = (rs, rowNum) -> {
        FacultyEntity entity = new FacultyEntity();

        entity.setId(rs.getObject("id", Long.class));
        entity.setName(rs.getString("name"));
        entity.setWebsite(rs.getString("website"));
        entity.setEmail(rs.getString("email"));
        entity.setPhone(rs.getString("phone"));
        entity.setAddress(rs.getString("address"));
        entity.setInfo(rs.getString("info"));

        return entity;
    };


    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public FacultyEntity create(FacultyEntity facultyEntity) {
        SqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("name", facultyEntity.getName())
                .addValue("website", facultyEntity.getWebsite())
                .addValue("email", facultyEntity.getEmail())
                .addValue("phone", facultyEntity.getPhone())
                .addValue("address", facultyEntity.getAddress())
                .addValue("info", facultyEntity.getInfo());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(INSERT_FACULTY_QUERY, sqlParameters, keyHolder);
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("duplicate key value violates unique constraint \"faculties_name_key\"")) {
                throw new DataConflictException(String.format("Faculty with name \"%s\" already exists!", facultyEntity.getName()));
            }

            throw e;
        }

        Long id = (Long) keyHolder.getKeys().get("id");
        facultyEntity.setId(id);

        return facultyEntity;
    }

    @Override
    public List<FacultyEntity> findAll(FacultyFilterOptions filterOptions, Integer limit, Integer offset) {
        StringBuilder queryBuilder = new StringBuilder(SELECT_FACULTIES_QUERY);
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        appendConditions(queryBuilder, parameters, filterOptions);

        if (limit != null) {
            queryBuilder.append(" LIMIT :limit");
            parameters.addValue("limit", limit);
        }

        if (offset != null && offset != 0) {
            queryBuilder.append(" OFFSET :offset");
            parameters.addValue("offset", offset);
        }

        String query = queryBuilder.toString();

        return jdbcTemplate.query(query, parameters, FACULTY_ROW_MAPPER);
    }

    @Override
    public int count(FacultyFilterOptions filterOptions) {
        StringBuilder queryBuilder = new StringBuilder(SELECT_FACULTY_COUNT_QUERY);
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        appendConditions(queryBuilder, parameters, filterOptions);

        String query = queryBuilder.toString();

        return jdbcTemplate.queryForObject(query, parameters, Integer.class);
    }

    @Override
    public FacultyEntity find(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_FACULTY_BY_ID_QUERY, new MapSqlParameterSource("id", id), FACULTY_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Faculty with id " + id + " not found!");
        }
    }

    @Override
    public void update(FacultyEntity facultyEntity) {
        int affectedRows;
        try {
            affectedRows = jdbcTemplate.update(UPDATE_FACULTY_BY_ID_QUERY, new MapSqlParameterSource()
                    .addValue("id", facultyEntity.getId())
                    .addValue("name", facultyEntity.getName())
                    .addValue("website", facultyEntity.getWebsite())
                    .addValue("email", facultyEntity.getEmail())
                    .addValue("phone", facultyEntity.getPhone())
                    .addValue("address", facultyEntity.getAddress())
                    .addValue("info", facultyEntity.getInfo())
            );
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("duplicate key value violates unique constraint \"faculties_name_key\"")) {
                throw new DataConflictException(String.format("Faculty with name \"%s\" already exists!", facultyEntity.getName()));
            }

            throw e;
        }

        if (affectedRows == 0) {
            throw new NotFoundException("Faculty with id " + facultyEntity.getId() + " not found!");
        }
    }

    @Override
    public void patch(Long id, FacultyPatch facultyPatch) {
        List<String> assignments = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);

        if (facultyPatch.isNameUpdated()) {
            assignments.add("name = :name");
            parameters.addValue("name", facultyPatch.getName());
        }

        if (facultyPatch.isWebsiteUpdated()) {
            assignments.add("website = :website");
            parameters.addValue("website", facultyPatch.getWebsite());
        }

        if (facultyPatch.isEmailUpdated()) {
            assignments.add("email = :email");
            parameters.addValue("email", facultyPatch.getEmail());
        }

        if (facultyPatch.isPhoneUpdated()) {
            assignments.add("phone = :phone");
            parameters.addValue("phone", facultyPatch.getPhone());
        }

        if (facultyPatch.isAddressUpdated()) {
            assignments.add("address = :address");
            parameters.addValue("address", facultyPatch.getAddress());
        }

        if (facultyPatch.isInfoUpdated()) {
            assignments.add("info = :info");
            parameters.addValue("info", facultyPatch.getInfo());
        }

        String assigmentStr = String.join(", ", assignments);
        String query = String.format(PATCH_FACULTY_BY_ID_QUERY_TEMPLATE, assigmentStr);

        int affectedRows;
        try {
            affectedRows = jdbcTemplate.update(query, parameters);
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("duplicate key value violates unique constraint \"faculties_name_key\"")) {
                throw new DataConflictException(String.format("Faculty with name \"%s\" already exists!", facultyPatch.getName()));
            }

            throw e;
        }

        if (affectedRows == 0) {
            throw new NotFoundException("Faculty with id " + id + " not found!");
        }
    }

    @Override
    public void delete(Long id) {
        int affectedRows = jdbcTemplate.update(DELETE_FACULTY_BY_ID_QUERY, new MapSqlParameterSource("id", id));

        if (affectedRows == 0) {
            throw new NotFoundException("Faculty with id " + id + " not found!");
        }
    }

    private void appendConditions(StringBuilder queryBuilder, MapSqlParameterSource parameters, FacultyFilterOptions filterOptions) {
        List<String> conditions = new ArrayList<>();

        String namePararm = filterOptions.getName();
        if (namePararm != null) {
            conditions.add("name LIKE(:name)");
            parameters.addValue("name", "%" + namePararm + "%");
        }

        String infoPararm = filterOptions.getInfo();
        if (infoPararm != null) {
            conditions.add("info LIKE(:info)");
            parameters.addValue("info", "%" + infoPararm + "%");
        }

        if (!conditions.isEmpty()) {
            String conditionStr = String.join(" AND ", conditions);

            queryBuilder.append(" WHERE ");
            queryBuilder.append(conditionStr);
        }
    }
}
