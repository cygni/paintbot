const getBlobSvgString = (width: number, height: number, colour: string) => {
  return `
    <svg x="0px" y="0px" width="${width}px" height="${height}px" xmlns="http://www.w3.org/2000/svg">
        <circle r="${width / 2}" cx="${width / 2}" cy="${height / 2}" fill="${colour}" stroke="black" stroke-width="4"/>
        <g fill="rgba(255,255,255,0.9)">
            <circle cx="${width / 2}" cy="${height - 10}" r="2.5"/>
            <circle cx="${width / 2}" cy="${height - 5}" r="1.5"/>
        </g>
    </svg>
    `;
};

const getObstacleSvgString = (width: number, height: number) => {
  return `
        <svg fill="orange" width="${width}" height="${height}" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" x="20px" y="0px"
            viewBox="0 0 297 297" style="enable-background:new 0 0 297 297;" xml:space="preserve">
            <path d="M286.966,17.308H10.034C4.493,17.308,0,21.801,0,27.342v103.85c0,5.541,4.493,10.034,10.034,10.034h24.583v118.399h-7.274
                c-5.541,0-10.034,4.493-10.034,10.034c0,5.541,4.493,10.034,10.034,10.034h34.617c5.541,0,10.034-4.493,10.034-10.034
                c0-5.541-4.493-10.034-10.034-10.034h-7.274V141.226h187.632v118.399h-7.274c-5.541,0-10.034,4.493-10.034,10.034
                c0,5.541,4.493,10.034,10.034,10.034h34.617c5.541,0,10.034-4.493,10.034-10.034c0-5.541-4.493-10.034-10.034-10.034h-7.274V141.226
                h24.583c5.541,0,10.034-4.493,10.034-10.034V27.342C297,21.801,292.507,17.308,286.966,17.308z M120.799,121.158l83.782-83.782
                h58.161l-83.782,83.782H120.799z M34.258,121.158l83.782-83.782h58.161l-83.782,83.782H34.258z M89.659,37.376l-69.592,69.592
                V37.376H89.659z M207.341,121.158l69.592-69.592v69.592H207.341z"/>
        </svg>

    `;
};

export { getBlobSvgString, getObstacleSvgString };
