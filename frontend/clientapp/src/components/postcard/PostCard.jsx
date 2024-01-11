import React from 'react'
//import reactLogo from '../../assets/react.svg'


const PostCard = (postInfo) => {
    return (
        <article>
            <div>
                <img src={`post/${postInfo["slug"]}/${postInfo["featuredImage"]}`} loading="lazy" width="30" height="30"/>
            </div>
            <div>
                <header>
                    <h2>{postInfo["title"]}</h2>
                </header>
                <div>
                    <p>{postInfo["excerpt"]}</p>
                    <a href = {`/post/${postInfo["slug"]}`}>Leer Mas</a>
                </div>
                <footer>
                    <p>{postInfo["users"]["name"]}</p>
                    <p>{postInfo["date"]}</p>
                </footer>
            </div>            
        </article>
    )
}

export default PostCard