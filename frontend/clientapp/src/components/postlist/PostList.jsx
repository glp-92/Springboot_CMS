import React from 'react'
import PostCard from '../postcard/PostCard'

const PostList = ({postArr}) => {
    return (
        <div>
            {postArr.map(item => (
                <div key={item["id"]}>{PostCard(item)}</div>
            ))}
        </div>
    )
}

export default PostList