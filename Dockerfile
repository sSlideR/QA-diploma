FROM node:8.16.2-alpine
WORKDIR /gate-simulator/
COPY artifacts/gate-simulator .
RUN npm install
CMD ["npm", "start"]
EXPOSE 9999