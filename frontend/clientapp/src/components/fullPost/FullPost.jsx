import React from 'react'
import Markdown from 'markdown-to-jsx'

const FullPost = ({ postData }) => {

    const overrideContentImagePath = (props) => { // Funcion que provee la libreria markdown-to-jsx para sobreescribir la ruta de imagen que debe tener el directorio /post/slug/... y por defecto solo trae el nombre de la BBDD
        const overridesrc = `${postData["slug"]}/${props.src}`;
        const overrideProps = { ...props, src: overridesrc, loading: "lazy" };
        return <img {...overrideProps} />;
    }

    const categories = postData["categories"].map((categorie) => <p className='categorie' key={categorie["id"]}>{categorie["name"]}</p>);

    return (
        <article>
            <header>
                <h1>{postData["title"]}</h1>
                <img src={`${postData["slug"]}/${postData["featuredImage"]}`} loading="lazy" width="120" height="120" />
            </header>
            <main>
                <Markdown options={{
                    overrides: {
                        img: overrideContentImagePath
                    }
                }}>
                    {postData["content"]}
                </Markdown>
            </main>
            <footer>
                <p>{postData["users"]["name"]}</p>
                {categories}
                <p>{postData["date"]}</p>
            </footer>
        </article>
    )
}

export default FullPost