FROM growerp/flutter-sdk-image:3.16.0 AS build

RUN git config --global --add safe.directory /flutter

COPY . /app
WORKDIR /app

USER root
RUN chown -R mobiledevops /app
USER mobiledevops

RUN flutter doctor -v
RUN flutter config --enable-web
RUN flutter pub get
RUN flutter build web --release

FROM nginx:stable-alpine3.17-slim
COPY --from=build /app/build/web /usr/share/nginx/html