<!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <title>Visualizador de Power BI Movil</title>
        
        <!-- Librería oficial de Power BI Client -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/powerbi-client/2.22.3/powerbi.min.js"></script>

        <style>
            html, body {
                margin: 0;
                padding: 0;
                width: 100%;
                height: 100%;
                background-color: #f4f6f8;
                overflow: hidden;
            }

            /* Contenedor adaptado a pantalla completa para móviles */
            #reportContainer {
                width: 100%;
                height: 100%;
                border: none;
            }
        </style>
    </head>
    <body>

        <div id="reportContainer"></div>

        <script>
            const powerbiClient = window['powerbi-client'];

            if (!powerbiClient) {
                console.error('No se pudo cargar la librería powerbi-client.');
            }

            const models = powerbiClient.models;

            const reportConfig = {
                 type: 'visual',
                tokenType: models.TokenType.Embed, // Usa Embed para tokens temporales devueltos por la API
                accessToken: `H4sIAAAAAAAEACWTR66DVgAA7_K3RKIXR8qC3jsYzA5jML3z4BHl7vlR9rMazfz94-Wwn_LPz58_uOdnkKn0Oqnp9kACwpa6OWn0ytzWgGeWSTgKwg1JealGqdbtJjwHn-wnr06ioqP6a56FL71loq5HjFoZaozlGK6TH9m5-N5h86Il34w8lKfSOjbw55x7RFqqS3dRMywi-iSHzCwtxQiDOcIDP5b8cFnboptGK_FWi51G6kaewgmN93GxpQVMR3KvwIDfdMNnQpvT3GAC07hlkGuOV7VloGoy9rarIHh349Iem-2mXKN-BlMvPwLt-wYy4zXN-1Nh2jUd6MNGuLt_6xHwB5w__R5Yho1KifoY1nerOIb22UwmQvNPirsQn-b2RfYzMaG9D10dpfBpR0bS05B7gBczTCYDkokPbvMkvvVrhARBXTBQsJp4Ps4CvMsUvo3A1Y6GGt4mGNrkGBSOHJEvvrRyk45rit82pQaC--RCGxx78uWDNiSldJ1uSkqNZRmlF3h5FdbiRTJquqNak1mZL8JW3S_TedJ1cvfM86abu64KXrjLYYt_18_e5pW71z3doU4w2DXphkJmci2AdTgFtLtAWxO40HBYIwQPAD43wbcWKSE-_pUovm8xrmseRWFtmL0JtT2KOGI-ML_gZGUu2YgtyRZLCv3MSCHTMNmQvVKYOFsJk_e99rIZV8B71fvZLLxoBqbxUnsRVbb9t8FqvFKrp3j2uF5jKuuTnfZFtsWWHtU5n9JkZbbfmWOjaFgmQsaw5VrOqVAMdrdM4or7BFSV5vJiQzjxECrU0nNYOZ2ClcBFRVRQrKlB61p3fsbiCcYSdK0JDkHg0qDTstUWnkiOe-dLlYISVgUbTZCnuq_G-t-h36-b71bTa_uFMegIoNGN4D9__IgrnPfJLOHvTg4dBihTLSJzBczFHtncREGWzNOaR0LGERssFd3Qqjt7PAsZOVgTI-NbV26u8J8fIrw0-_wAE9xql28-KtXTIGO8s5u9p4H5zJFz40Uatuwwr7rcXr4mDCn2IlMBZbqnSdiawdxYo0t1Ld504Vf1Xan4o8wgyBtRSOmbal853CqY9byL5QEyPLdiaUFRu4ISbIhHnjWjd1DW_HohVV81lpcSPsbcOjxcaPd08ghEEN_9LGhLVvrrYjcJDTXTgHm-l4I98pjWPM-7Uwm3t-SLpKYVqR5Jhi4THuaIVtTczaj84ciht3d2mqm10s9dYVobq96JENRHPui6bzYncBDFJg3_r7_-0wznulz1569l-LRFuoLatpMpIU8Hm7PM-T8VNt8x34-1_MUq7bx3-t0_c6g1C5aVeEJOujL1gWckW0UpnIzaILFIm-jM8uDnRlBYy-JjGeMc1EKDCh3bnXgimz9JOLnutAXXK6p3RkHpopGsSNnWmEtZkUBiJ_7eewXJKLVwjcsrTR-l4issVKHuXUygNcm67pNhYxyHTnlAv3wwyb1yhnh2No4nzMuNyiM4pwZ8LXzLSODFgvy7fbbcQp489hFE3iq4K2Z8mCu8DyU5dafvOoFnjr402IDlcl04yLenn0iHxYCyPxR382rVVExxHXKoye_WcSj8aTBKgQpX48nvgO6WpE9ROBH-M-uE8c0lvZwaw-4nYeSXKCnq3k7S8-M7inu8i99fzf_8C6VJ18tCBgAA.eyJjbHVzdGVyVXJsIjoiaHR0cHM6Ly9XQUJJLVVTLUNFTlRSQUwtQi1QUklNQVJZLXJlZGlyZWN0LmFuYWx5c2lzLndpbmRvd3MubmV0IiwiZXhwIjoxNzkzNzY4OTg3LCJhbGxvd0FjY2Vzc092ZXJQdWJsaWNJbnRlcm5ldCI6dHJ1ZX0=`,
                embedUrl: `https://app.powerbi.com/reportEmbed?reportId=70342e8e-2254-4d20-9dc7-f58531f3f436&groupId=c129e462-68d4-40ce-8c8b-127f42f87d90&w=2&config=eyJjbHVzdGVyVXJsIjoiaHR0cHM6Ly9XQUJJLVVTLUNFTlRSQUwtQi1QUklNQVJZLXJlZGlyZWN0LmFuYWx5c2lzLndpbmRvd3MubmV0IiwiZW1iZWRGZWF0dXJlcyI6eyJ1c2FnZU1ldHJpY3NWTmV4dCI6dHJ1ZX19`,
                id: "70342e8e-2254-4d20-9dc7-f58531f3f436",
                visualName:"94f01a94d1d14caea990",
                pageName: "d87944050161b6940354", // Abre directamente la página específica
                settings: {
                    panes: {
                        filters: {
                            visible: true,
                            expanded: false
                        },
                        pageNavigation: {
                            visible: true,
                            position: models.PageNavigationPosition.Bottom
                        }
                    }
                }
            };

            const reportContainer = document.getElementById('reportContainer');
            const powerbiService = new powerbiClient.service.Service(
                powerbiClient.factories.hpmFactory,
                powerbiClient.factories.wpmpFactory,
                powerbiClient.factories.routerFactory
            );

            const report = powerbiService.embed(reportContainer, reportConfig);
        </script>
    </body>
    </html>