/*
 * Created on 17 Dec 2018 ( Time 16:17:21 )
 * Generated by Telosys Tools Generator ( version 2.1.1 )
 */
package com.fileService.service;

import com.core.entity.VbAttachment;
import com.core.repository.VbAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of VbAttachmentService
 */
@Service("vbAttachmentService")
@Transactional
public class VbAttachmentServiceImpl implements VbAttachmentService {

    @Autowired
    private VbAttachmentRepository vbAttachmentRepository;

    @Override
    public VbAttachment findById(Long id) {
        return vbAttachmentRepository.findById(id).get();
    }

    @Override
    public VbAttachment findByObjectId(Long id) {

        List<VbAttachment> list = vbAttachmentRepository.getAllByObject(id, 2l);

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public VbAttachment save(VbAttachment vbAttachment) {
        return vbAttachmentRepository.saveAndFlush(vbAttachment);
    }

}
