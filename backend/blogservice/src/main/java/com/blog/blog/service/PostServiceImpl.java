package com.blog.blog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
	public Post createPost(CreatePost request, String username) {
		User author = authorRepo.findByUsername(username).orElse(null);
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
	@Cacheable("firstPageCache")
	public List<Post> getAllPosts() {
		return postRepo.findAll();
	}

	@Override
	public boolean deletePost(String postId) {
		if (postRepo.existsById(Long.valueOf(postId))) {
			Post post = postRepo.findById(Long.valueOf(postId)).orElse(null);
			if (post != null) {
				try {
					String postImageFolderPath = Paths.get(storagePath).resolve(post.getSlug()).toString();
					FileUtils.deleteDirectory(new File(postImageFolderPath));
				}catch (Exception e) {
				    System.err.println("Error al eliminar el directorio del post: " + e.getMessage());
				}
			}
			postRepo.deleteById(Long.valueOf(postId));
			return !postRepo.existsById(Long.valueOf(postId));
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> getPostsFiltered(String keyword, int page, boolean reverse) {
		List<Post> results = new ArrayList<>();
		if (keyword != null) {
			Categorie categorie = categorieRepo.findByName(keyword);
			for (String key : Arrays.asList(keyword.split(","))) {
				results.addAll(postRepo.findByCategoriesContainingAndTitleContainingOrContentContainingIgnoreCase(categorie, key.trim(), key.trim()));
				results.addAll(postRepo.findByTitleContainingOrContentContainingIgnoreCase(key.trim(), key.trim()));	
			}
			results.addAll(postRepo.findByCategoriesContaining(categorie));
		}
		else {
			results.addAll(getAllPosts());	
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
		int totalPages = pageResult.getTotalPages();
		Map<String, Object> response = new HashMap<>();
	    response.put("totalPages", totalPages);
	    response.put("content", pageResult.getContent());
	    return response;
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
	        File directory = new File(imageStoragePath).getParentFile();;
	        if (!directory.exists()) {
	            directory.mkdirs();  // También puedes usar mkdir() si solo necesitas crear un directorio
	        }
	        File file = new File(imageStoragePath);
	        image.transferTo(file);
	        imageFullPathList.add(imageStoragePath);
	    }
		return imageFullPathList;
	}

}
