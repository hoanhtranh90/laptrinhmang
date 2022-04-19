package com.fileService.controller;

import com.core.model.file.GetNumberPagesOfPdfResponse;
import com.fileService.service.AttachDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachDocument")
public class AttachDocumentController {

    @Autowired
    private AttachDocumentService attachDocumentService;

    @GetMapping("/{id}/getNumberPagesOfPdf")
    public GetNumberPagesOfPdfResponse getNumberPagesOfPdf(@PathVariable Long id) {
        return attachDocumentService.getNumberPagesOfPdf(id);
    }
}
