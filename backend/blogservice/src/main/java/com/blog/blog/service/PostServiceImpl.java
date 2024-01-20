package com.blog.blog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog.blog.data.UserRepository;
import com.blog.blog.data.CategorieRepository;
import com.blog.blog.data.PostRepository;
import com.blog.blog.model.pojo.User;
import com.blog.blog.model.pojo.Categorie;
import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.request.CreatePost;
import com.blog.blog.model.request.EditPost;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private CategorieRepository categorieRepo;
	
	@Autowired
	private UserRepository authorRepo;
	
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	@Value("${storage-path}")
    private String storagePath;
    
	@Override
	public Post createPost(CreatePost request) {
		User author = authorRepo.findById(request.getAuthorId()).orElse(null);
		Date date = new Date(System.currentTimeMillis());
		Set<Categorie> categories = new HashSet<Categorie>(categorieRepo.findAllById(request.getCategoryIds()));
		Post post = Post.builder().title(request.getTitle()).slug(request.getSlug()).excerpt(request.getExcerpt()).content(request.getContent()).date(date).featuredImage(request.getFeaturedImage()).featuredPost(request.getFeaturedPost()).categories(categories).users(author).build();
		return postRepo.save(post);
	}
	
	@Override
	public Post editPost(EditPost request) {
		Optional<Post> post_opt = postRepo.findById(Long.valueOf(request.getPostId()));
		if (post_opt.isEmpty()) {
            return null;
        }
		Post post = post_opt.get();
		if (request.getTitle() != null) {
	        post.setTitle(request.getTitle());
	    }
		if (request.getContent() != null) {
	        post.setContent(request.getContent());
	    }
		if (request.getExcerpt() != null) {
			post.setExcerpt(request.getExcerpt());
		}
		if (request.getFeaturedImage() != null) {
			post.setExcerpt(request.getFeaturedImage());
		}
		if (request.getSlug() != null) {
			post.setSlug(request.getSlug());
		}
		if (request.getFeaturedPost() != null) {
	        post.setFeaturedPost(request.getFeaturedPost());
	    }
		if (request.getDate() != null) {
			Date date = null;
			try {
			    date = dateFormatter.parse(request.getDate());
			} catch (ParseException e) {
				date = new Date(System.currentTimeMillis());
			}
			post.setDate(date);
		}
		if (request.getCategoryIds() != null) {
			Set<Categorie> categories = new HashSet<Categorie>(categorieRepo.findAllById(request.getCategoryIds()));
			post.setCategories(categories);
		}
		return postRepo.save(post);
	}
	
	@Override
	public List<Post> getAllPosts() {
		return postRepo.findAll();
	}

	@Override
	public boolean deletePost(String postId) {
		if (postRepo.existsById(Long.valueOf(postId))) {
			postRepo.deleteById(Long.valueOf(postId));
			return !postRepo.existsById(Long.valueOf(postId));
		} else {
			return false;
		}
	}

	@Override
	public List<Post> getPostsFiltered(String categorieName, String keyword, int page, boolean reverse) {
		List<Post> results = new ArrayList<>();
		
		if (categorieName != null) {
			Categorie categorie = categorieRepo.findByName(categorieName);
			if (keyword != null) {
				for (String key : Arrays.asList(keyword.split(","))) {
					results.addAll(postRepo.findByCategoriesContainingAndTitleContainingOrContentContainingIgnoreCase(categorie, key.trim(), key.trim()));
				}
			} else {
				results.addAll(postRepo.findByCategoriesContaining(categorie));
			}
		} else if (keyword != null) {
			for (String key : Arrays.asList(keyword.split(","))) {
				results.addAll(postRepo.findByTitleContainingOrContentContainingIgnoreCase(key.trim(), key.trim()));	
			}
		} else {
			results.addAll(postRepo.findAll());	
		}
		
		Set<Long> postIds = new HashSet<>();
	    for (Post post : results) {
	        if (!postIds.contains(post.getId())) {
	            postIds.add(post.getId());
	        }
	    }
	    Sort.Direction direction = reverse ? Sort.Direction.ASC: Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, 5, Sort.by(direction, "date"));
		Page<Post> pageResult = postRepo.findAllByIdIn(postIds, pageable);
		/*int totalPages = pageResult.getTotalPages();
		System.out.println(totalPages);
		Map<String, Object> response = new HashMap<>();
		response.put("totalPages", pageResult.getTotalPages());
		response.put("content", pageResult.getContent());*/
		return pageResult.getContent();
	}

	@Override
	public Post getPostByUri(String postSlug) {
		return postRepo.findBySlug(postSlug);
	}

	@Override
	public List<String> uploadImages(List<MultipartFile> imageList, List<String> imagenameList) throws IllegalStateException, IOException {
		if (imageList == null || imagenameList == null || imageList.size() != imagenameList.size()) {
		    throw new IllegalStateException("Las listas de imágenes y nombres deben tener la misma longitud y no vacias");
		}
		List<String> imageFullPathList = new ArrayList<>();
	    for (int i = 0; i < imageList.size(); i++) {
	        MultipartFile image = imageList.get(i);
	        String imageStoragePath = storagePath + imagenameList.get(i);
	        File file = new File(imageStoragePath);
	        image.transferTo(file);
	        imageFullPathList.add(imageStoragePath);
	    }
		return imageFullPathList;
	}

}
