export const cleanNonBlobFromArr = (images, imageNames) => {
    const imagesToRemove = [];
    images.forEach((image, index) => {
        if(!(image instanceof Blob)) {
        imagesToRemove.push(index);
        }
    })
    imagesToRemove.reverse().forEach(index => {
        images.splice(index, 1);
        imageNames.splice(index, 1);
    })
    return images, imageNames;
};