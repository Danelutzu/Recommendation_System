from rest_framework.views import APIView
from rest_framework.response import Response
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel


class RecommendationSystemView(APIView):
    def post(self, request):
        grouped_data = request.data['groupedData']
        product_name = request.data['productName']

        formatted_data = []
        for item in grouped_data:
            user_id = str(item['userId'])
            product = item['productName']
            categories = [category.capitalize() for category in item['categories']]

            formatted_data.append((user_id, product, categories))

        recommendations = recommendation_ai_system(formatted_data, product_name)

        # Generate a response
        response_data = {'recommendations': recommendations}

        return Response(response_data)


def recommendation_ai_system(data, product_id):
    print('AI: ', product_id)
    # data = [
    #     ('user1', 'Laptop', ['Electronics', 'Gadgets']),
    #     ('user1', 'Blouse', ['Clothing', 'Fashion']),
    #     ('user2', 'Charger', ['Electronics', 'Gadgets']),
    #     ('user2', 'Forks', ['Kitchen', 'Cookware']),
    #     ('user3', 'Trousers', ['Clothing', 'Fashion']),
    #     ('user3', 'Spoon', ['Kitchen', 'Cookware']),
    #     ('user4', 'Phone', ['Electronics', 'Gadgets']),
    #     ('user4', 'Plates', ['Kitchen', 'Cookware']),
    #     ('user4', 'Microwave', ['Electronics', 'Cookware']),
    # ]
    # product_id = 'Microwave'
    num_recommendations = 4
    # Create a DataFrame
    # Creez un tabel din data cu row index asignati automat
    df = pd.DataFrame(data, columns=['UserID', 'ProductID', 'Categories'])
    # print(df)
    # print("\n")

    # Convert categories to strings
    # Convertesc lista de categorii in stringuri separate prin spatiu(ca sa poata fi compatibile cu algoritmul de vectorizare
    # Adica din ['Electronics', 'Gadgets'] => 'Electronics Gadgets'
    df['Categories'] = df['Categories'].apply(lambda cat_list: ' '.join(cat_list))
    # print(df['Categories'])
    # print("\n")

    # Initialize TF-IDF Vectorizer
    # pt setul de date
    # data = [
    #     ('user1', 'Laptop', ['Electronics', 'Gadgets']),
    #     ('user1', 'Blouse', ['Clothing', 'Fashion']),
    #     ('user2', 'Charger', ['Electronics', 'Gadgets']),
    #     ('user2', 'Forks', ['Kitchen', 'Cookware']),
    #     ('user3', 'Trousers', ['Clothing', 'Fashion']),
    #     ('user3', 'Spoon', ['Kitchen', 'Cookware']),
    #     ('user4', 'Phone', ['Electronics', 'Gadgets']),
    #     ('user4', 'Plates', ['Kitchen', 'Cookware']),
    #     ('user4', 'Microwave', ['Electronics', 'Cookware']),
    # ]

    # prima data se cauta in intreg vocabularul unique terms si se asigneaza un index
    # pt setul de date de mai sus: 'Electronics' e primul unique si se asigneaza index 0
    # 'Gadgets' e urmatorul unic si se asigneaza 1 etc

    # tf-idf pentru 'Clothing' =

    # (pt a avea o intrare a term ului in tfidf_matrix se calculeaza pentru fiecare produs care contine acest term)

    # Caut produsele in care apare termul 'Clothing') => Blouse(index 1) si Trousers(index 4)

    # calculez pt fiecare produs

    # pt produsul cu index 1:

    # TF('Clothing) * IDF('Clothing')
    # TF('Clothing') = 0.5 = (1/2)
    # (pentru ca in documentul(lista de categorii a produsului 1 apare o singura data si sunt 2 categorii in lista)

    # IDF('Clothing') = log(9/2) (pentru ca sunt 9 produse si doar in doua se afla 'Clothing')

    # TF-DF('Clothing') = 0.5 * log(9/2)(log e in baza e) = 0.6621277371895199

    # dupa convertirea listelor de categorii la space separated strings avem:
    # 'Electronics Gadgets'
    # 'Clothing Fashion'
    # 'Electronics Gadgets'
    # 'Kitchen Cookware'
    # 'Clothing Fashion'
    # 'Kitchen Cookware'
    # 'Electronics Gadgets'
    # 'Kitchen Cookware'
    # 'Electronics Cookware'

    # TF pentru 'Electronics' = 4(apare de 4 ori in toate produsele)
    # IDF(t) = log(N/nt) unde:
    # t:-term('Electroniucs')
    # N: numarul total de produse
    # nt: numarul de produse care contin t

    # IDF('Electronics') = log(9/4) = 0.5108
    # TF-IDF('Electronics') = TF('Electronics') * IDF('Electronics')
    # = 4 * 0.5108 = 2.0432
    tfidf_vectorizer = TfidfVectorizer()
    # print("tfidf_vectorizer\n")
    # print(tfidf_vectorizer)
    # print("\n")

    # Transform category data into TF-IDF features
    tfidf_matrix = tfidf_vectorizer.fit_transform(df['Categories'])
    # print("tfidf_matrix\n")
    # print(tfidf_matrix)
    # print("\n")

    # Compute cosine similarities between items
    cosine_similarities = linear_kernel(tfidf_matrix, tfidf_matrix)
    # print("cosine_similarities\n")
    # print(cosine_similarities)
    # print("\n")

    # Create a mapping of ProductID to index
    product_indices = pd.Series(df.index, index=df['ProductID']).to_dict()
    # print("product_indices\n")
    # print(product_indices)
    # print("\n")

    # Function to get recommendations based on content similarity
    def get_recommendations(product_id, num_recommendations, product_indices):
        idx = product_indices[product_id]
        # print("idx\n")
        # print(idx)
        # print("\n")
        sim_scores = cosine_similarities[idx, :]
        # print("sim_scores\n")
        # print(sim_scores)
        # print("\n")

        # Get the indices of similar products
        similar_indices = sim_scores.argsort()[-num_recommendations:]
        similar_indices = [index for index in similar_indices if index != idx]
        # print("similar_indices\n")
        # print(similar_indices)
        # print("\n")

        # Get the product IDs of similar products
        similar_products = [df['ProductID'][index] for index in similar_indices]
        # print("similar_products\n")
        # print(similar_products)
        # print("\n")

        # Filter recommendations to include only products from the same categories
        chosen_product_categories = df.loc[df['ProductID'] == product_id, 'Categories'].iloc[0]
        # print("chosen_product_categories\n")
        # print(chosen_product_categories)
        # print("\n")
        recommended_products = [product for product in similar_products if product != product_id and any(
            cat in df.loc[df['ProductID'] == product, 'Categories'].iloc[0] for cat in
            chosen_product_categories.split())]

        return recommended_products

    # Example usage

    recommendations = get_recommendations(product_id, num_recommendations, product_indices)
    print("Recommended products for", product_id, ":", recommendations)

    return recommendations
