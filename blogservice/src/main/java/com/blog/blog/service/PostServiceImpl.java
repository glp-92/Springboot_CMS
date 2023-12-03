package com.blog.blog.service;

import java.util.Date;
import java.util.HashSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
	@Override
	public Post createPost(CreatePost request) {
		User author = authorRepo.findById(request.getAuthorId()).orElse(null);
		Date date = null;
		try {
		    date = dateFormatter.parse(request.getDate());
		} catch (ParseException e) {
			date = new Date(System.currentTimeMillis());
		}
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

}
