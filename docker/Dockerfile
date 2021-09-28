FROM adoptopenjdk/openjdk16:debian-jre

#copy the package jar into docker image
COPY ecommerce-all.jar ecommerce.jar

#set startup command to execute jar
CMD ["java","-jar","ecommerce.jar"]
