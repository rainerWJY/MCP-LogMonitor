<script setup lang="ts">
import HelloWorld from '@/components/HelloWorld.vue'
</script>

<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.svg">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
    
    <!-- Dashboard Link -->
    <div class="mt-8">
      <router-link 
        to="/dashboard" 
        class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        查看监控仪表板
      </router-link>
    </div>
  </div>
</template>
