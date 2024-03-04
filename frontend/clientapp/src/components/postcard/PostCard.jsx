import React from 'react'
//import reactLogo from '../../assets/react.svg'

const PostCard = (postData) => {

    const categories = postData["categories"].map((categorie) => <p className='categorie' key={categorie["id"]}>{categorie["name"]}</p>);

    return (
        <article>
            <div>
                <img src={`post/${postData["slug"]}/${postData["featuredImage"]}`} loading="lazy" width="30" height="30" />
            </div>
            <div>
                <header>
                    <h2>{postData["title"]}</h2>
                </header>
                <div>
                    <p>{postData["excerpt"]}</p>
                    <a href={`/post/${postData["slug"]}`}>Leer Mas</a>
                </div>
                <footer>
                    <p>{postData["users"]["name"]}</p>
                    {categories}
                    <p>{postData["date"]}</p>
                </footer>
            </div>
        </article>
    )
}

export default PostCard