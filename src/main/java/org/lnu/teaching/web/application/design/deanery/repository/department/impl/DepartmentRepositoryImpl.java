package org.lnu.teaching.web.application.design.deanery.repository.department.impl;

import lombok.AllArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentPatch;
import org.lnu.teaching.web.application.design.deanery.dto.department.query.params.DepartmentFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.repository.department.DepartmentRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private static final String INSERT_DEPARTMENT_QUERY = """
            INSERT INTO departments (
                name,
                faculty_id,
                email,
                phone,
                info
            ) VALUES (
                :name,
                :facultyId,
                :email,
                :phone,
                :info
            )
            """;

    private static final String SELECT_DEPARTMENTS_QUERY = """
            SELECT
                id,
                name,
                faculty_id,
                email,
                phone,
                info
            FROM departments
            """;

    private static final String SELECT_DEPARTMENT_COUNT_QUERY = """
            SELECT COUNT(1) FROM departments
            """;

    private static final String SELECT_DEPARTMENT_BY_ID_QUERY = """
            SELECT
                id,
                name,
                faculty_id,
                email,
                phone,
                info
            FROM departments
            WHERE id = :id
            """;

    private static final String UPDATE_DEPARTMENT_BY_ID_QUERY = """
            UPDATE departments SET
                name = :name,
                faculty_id = :facultyId,
                email = :email,
                phone = :phone,
                info = :info
            WHERE id = :id
            """;

    private static final String PATCH_DEPARTMENT_BY_ID_QUERY_TEMPLATE = """
            UPDATE departments SET
                %s
            WHERE id = :id
            """;

    private static final String DELETE_DEPARTMENT_BY_ID_QUERY = """
            DELETE FROM departments WHERE id = :id
            """;

    private static final RowMapper<DepartmentEntity> DEPARTMENT_ROW_MAPPER = (rs, rowNum) -> {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setId(rs.getObject("id", Long.class));
        entity.setName(rs.getString("name"));
        entity.setFacultyId(rs.getObject("faculty_id", Long.class));
        entity.setEmail(rs.getString("email"));
        entity.setPhone(rs.getString("phone"));
        entity.setInfo(rs.getString("info"));
        return entity;
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public DepartmentEntity create(DepartmentEntity departmentEntity) {
        SqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("name", departmentEntity.getName())
                .addValue("facultyId", departmentEntity.getFacultyId())
                .addValue("email", departmentEntity.getEmail())
                .addValue("phone", departmentEntity.getPhone())
                .addValue("info", departmentEntity.getInfo());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(INSERT_DEPARTMENT_QUERY, sqlParameters, keyHolder);
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("duplicate key value violates unique constraint \"departments_name_key\"")) {
                throw new DataConflictException(String.format("Department with name \"%s\" already exists!", departmentEntity.getName()));
            }
            throw e;
        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getCause().getMessage();
            if (errorMessage.contains("Key (faculty_id)=") && errorMessage.contains("is not present in table \"faculties\"")) {
                throw new NotFoundException("Faculty with id " + departmentEntity.getFacultyId() + " does not exist!");
            }

            throw e;
        }

        Long id = (Long) keyHolder.getKeys().get("id");
        departmentEntity.setId(id);

        return departmentEntity;
    }

    @Override
    public List<DepartmentEntity> findAll(DepartmentFilterOptions filterOptions, Integer limit, Integer offset) {
        StringBuilder queryBuilder = new StringBuilder(SELECT_DEPARTMENTS_QUERY);
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

        return jdbcTemplate.query(queryBuilder.toString(), parameters, DEPARTMENT_ROW_MAPPER);
    }

    @Override
    public int count(DepartmentFilterOptions filterOptions) {
        StringBuilder queryBuilder = new StringBuilder(SELECT_DEPARTMENT_COUNT_QUERY);
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        appendConditions(queryBuilder, parameters, filterOptions);

        return jdbcTemplate.queryForObject(queryBuilder.toString(), parameters, Integer.class);
    }

    @Override
    public DepartmentEntity find(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_DEPARTMENT_BY_ID_QUERY, new MapSqlParameterSource("id", id), DEPARTMENT_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Department with id " + id + " not found!");
        }
    }

    @Override
    public void update(DepartmentEntity departmentEntity) {
        int affectedRows;
        try {
            affectedRows = jdbcTemplate.update(UPDATE_DEPARTMENT_BY_ID_QUERY, new MapSqlParameterSource()
                    .addValue("id", departmentEntity.getId())
                    .addValue("name", departmentEntity.getName())
                    .addValue("facultyId", departmentEntity.getFacultyId())
                    .addValue("email", departmentEntity.getEmail())
                    .addValue("phone", departmentEntity.getPhone())
                    .addValue("info", departmentEntity.getInfo())
            );
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("duplicate key value violates unique constraint \"departments_name_key\"")) {
                throw new DataConflictException(String.format("Department with name \"%s\" already exists!", departmentEntity.getName()));
            }
            throw e;
        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getCause().getMessage();
            if (errorMessage.contains("Key (faculty_id)=") && errorMessage.contains("is not present in table \"faculties\"")) {
                throw new NotFoundException("Faculty with id " + departmentEntity.getFacultyId() + " does not exist!");
            }

            throw e;
        }

        if (affectedRows == 0) {
            throw new NotFoundException("Department with id " + departmentEntity.getId() + " not found!");
        }
    }

    @Override
    public void patch(Long id, DepartmentPatch departmentPatch) {
        List<String> assignments = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);

        if (departmentPatch.isNameUpdated()) {
            assignments.add("name = :name");
            parameters.addValue("name", departmentPatch.getName());
        }

        if (departmentPatch.isFacultyIdUpdated()) {
            assignments.add("faculty_id = :facultyId");
            parameters.addValue("facultyId", departmentPatch.getFacultyId());
        }

        if (departmentPatch.isEmailUpdated()) {
            assignments.add("email = :email");
            parameters.addValue("email", departmentPatch.getEmail());
        }

        if (departmentPatch.isPhoneUpdated()) {
            assignments.add("phone = :phone");
            parameters.addValue("phone", departmentPatch.getPhone());
        }

        if (departmentPatch.isInfoUpdated()) {
            assignments.add("info = :info");
            parameters.addValue("info", departmentPatch.getInfo());
        }

        String query = String.format(PATCH_DEPARTMENT_BY_ID_QUERY_TEMPLATE, String.join(", ", assignments));

        int affectedRows;
        try {
            affectedRows = jdbcTemplate.update(query, parameters);
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("duplicate key value violates unique constraint \"departments_name_key\"")) {
                throw new DataConflictException(String.format("Department with name \"%s\" already exists!", departmentPatch.getName()));
            }
            throw e;
        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getCause().getMessage();
            if (errorMessage.contains("Key (faculty_id)=") && errorMessage.contains("is not present in table \"faculties\"")) {
                throw new NotFoundException("Faculty with id " + departmentPatch.getFacultyId() + " does not exist!");
            }

            throw e;
        }

        if (affectedRows == 0) {
            throw new NotFoundException("Department with id " + id + " not found!");
        }
    }

    @Override
    public void delete(Long id) {
        int affectedRows = jdbcTemplate.update(DELETE_DEPARTMENT_BY_ID_QUERY, new MapSqlParameterSource("id", id));

        if (affectedRows == 0) {
            throw new NotFoundException("Department with id " + id + " not found!");
        }
    }

    private void appendConditions(StringBuilder queryBuilder, MapSqlParameterSource parameters, DepartmentFilterOptions filterOptions) {
        List<String> conditions = new ArrayList<>();

        if (filterOptions.getName() != null) {
            conditions.add("name LIKE(:name)");
            parameters.addValue("name", "%" + filterOptions.getName() + "%");
        }

        if (filterOptions.getFacultyId() != null) {
            conditions.add("faculty_id = :facultyId");
            parameters.addValue("facultyId", filterOptions.getFacultyId());
        }

        if (filterOptions.getInfo() != null) {
            conditions.add("info LIKE(:info)");
            parameters.addValue("info", "%" + filterOptions.getInfo() + "%");
        }

        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }
    }
}
