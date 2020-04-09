package ru.skillbox.blog_engine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.TagDto;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.Tag;
import ru.skillbox.blog_engine.repository.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private EntityMapper entityMapper;

    public TagService(TagRepository tagRepository, PostService postService,
                      EntityMapper entityMapper) {
        this.tagRepository = tagRepository;
        this.postService = postService;
        this.entityMapper = entityMapper;
    }

    public List<TagDto> getTagDtoListByQuery(String query) {
        return getAllTagDtoList().stream()
                .filter(tag -> tag.getName().toLowerCase().startsWith(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Tag> getAllTagsFromRepository() {
        List<Tag> tagList = new ArrayList<>();
        tagRepository.findAll().forEach(tagList::add);
        return tagList;
    }

    private List<TagDto> getAllTagDtoList() {
        List<Post> allPostList = postService.getAllPostsFromRepository(true, ModerationStatus.ACCEPTED);
        List<Tag> allTagList = getAllTagsFromRepository();
        return allTagList.stream()
            .map(tag -> entityMapper.tagToTagDto(tag, allPostList.size()))
            .collect(Collectors.toList());
    }

    public Tag saveTag(String tagName) {
        Tag tag = tagRepository.findByNameIgnoreCase(tagName);
        return (tag != null) ? tag : tagRepository.save(new Tag(tagName));
    }
}
