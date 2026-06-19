package com.luthfii.libraryapi.service;

import com.luthfii.libraryapi.dto.request.MemberRequest;
import com.luthfii.libraryapi.dto.response.MemberResponse;

import java.util.List;

public interface MemberService {

    MemberResponse create(MemberRequest request);
    MemberResponse getById(Long id);
    List<MemberResponse> getAll();
    MemberResponse update(Long id, MemberRequest request);
    void delete(Long id);

}
