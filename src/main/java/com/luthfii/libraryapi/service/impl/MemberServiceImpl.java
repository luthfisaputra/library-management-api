package com.luthfii.libraryapi.service.impl;

import com.luthfii.libraryapi.dto.request.MemberRequest;
import com.luthfii.libraryapi.dto.response.MemberResponse;
import com.luthfii.libraryapi.entity.Member;
import com.luthfii.libraryapi.exception.DuplicateResourceException;
import com.luthfii.libraryapi.exception.ResourceNotFoundException;
import com.luthfii.libraryapi.repository.MemberRepository;
import com.luthfii.libraryapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberResponse create(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email sudah terdaftar");
        }

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        member.setJoinedAt(LocalDate.now());
        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }

    @Override
    public MemberResponse getById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member tidak ditemukan"));
        return toResponse(member);
    }

    @Override
    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse update(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member tidak ditemukan"));

        if (!member.getEmail().equals(request.getEmail())
                && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email sudah terdaftar");
        }

        member.setName(request.getName());
        member.setEmail(request.getEmail());

        return toResponse(memberRepository.save(member));
    }

    @Override
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member tidak ditemukan"));
        memberRepository.delete(member);
    }

    private MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
