from django.urls import path
from .views import RecommendationSystemView

urlpatterns = [
    path('recommend', RecommendationSystemView.as_view()),
]
