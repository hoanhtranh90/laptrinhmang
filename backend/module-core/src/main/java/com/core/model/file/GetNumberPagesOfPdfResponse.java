package com.core.model.file;

public class GetNumberPagesOfPdfResponse {

    private Long id; //attachDocumentId
    private Long numberOfPages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Long numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String toString() {
        return "GetNumberPagesOfPdfResponse{"
                + "id='" + id + '\''
                + ", numberOfPages=" + numberOfPages
                + '}';
    }
}
