package org.example.services;

import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * Captures user-supplied catalog filters with helpers for defaults and validation.
 */
public class BookSearchCriteria {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 12;
    private static final String DEFAULT_SORT_PROPERTY = "title";
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.ASC;
    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of("title", "author", "price");

    private String searchTerm;
    private String publisher;
    private String tag;
    private String sortBy;
    private Sort.Direction sortDirection;
    private Integer page;
    private Integer size;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean hasSearchTerm() {
        return StringUtils.hasText(searchTerm);
    }

    public boolean hasPublisher() {
        return StringUtils.hasText(publisher);
    }

    public boolean hasTag() {
        return StringUtils.hasText(tag);
    }

    public int resolvedPage() {
        return page != null && page >= 0 ? page : DEFAULT_PAGE;
    }

    public int resolvedSize() {
        return size != null && size > 0 ? size : DEFAULT_PAGE_SIZE;
    }

    public Sort.Direction resolvedDirection() {
        return sortDirection != null ? sortDirection : DEFAULT_DIRECTION;
    }

    public String resolvedSortProperty() {
        if (StringUtils.hasText(sortBy) && ALLOWED_SORT_PROPERTIES.contains(sortBy.toLowerCase())) {
            return sortBy.toLowerCase();
        }
        return DEFAULT_SORT_PROPERTY;
    }

    public Sort toSort() {
        return Sort.by(resolvedDirection(), resolvedSortProperty());
    }
}
